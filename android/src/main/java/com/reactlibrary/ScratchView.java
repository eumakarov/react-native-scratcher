package com.como.RNTScratchView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import java.util.ArrayList;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.io.InputStream;
import java.net.URL;

public class ScratchView extends View implements View.OnTouchListener {
    boolean imageTakenFromView = false;
    float threshold = 0;
    float brushSize = 0;
    String imageUrl = null;
    String resourceName = null;
    String resizeMode = "stretch";
    Bitmap image;
    Path path;
    float minDimension;
    float gridSize;
    ArrayList<ArrayList<Boolean>> grid;
    boolean cleared;
    int clearPointsCounter;
    float scratchProgress;
    int placeholderColor = -1;

    Paint imagePaint = new Paint();
    Paint pathPaint = new Paint();

    boolean inited = false;

    Rect imageRect = null;

    public ScratchView(Context context) {
        super(context);
        init();
    }

    public ScratchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScratchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(this);

        imagePaint.setAntiAlias(true);
        imagePaint.setFilterBitmap(true);

        pathPaint.setAlpha(0);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        pathPaint.setAntiAlias(true);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setPlaceholderColor(@Nullable String placeholderColor) {
        if (placeholderColor != null) {
            try {
                this.placeholderColor = Color.parseColor(placeholderColor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public void setBrushSize(float brushSize) {
        this.brushSize = brushSize * 3;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setResizeMode(String resizeMode) {
        if (resizeMode != null) {
            this.resizeMode = resizeMode.toLowerCase();
        }
    }

    private void loadImage() {
        path = null;
        if (imageUrl != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream is = (InputStream) new URL(imageUrl).getContent();
                        image = BitmapFactory.decodeStream(is).copy(Bitmap.Config.ARGB_8888, true);
                        reportImageLoadFinished(true);
                        invalidate();

                    } catch (Exception e) {
                        reportImageLoadFinished(false);
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else if (resourceName != null) {
            int imageResourceId = getResources().getIdentifier(resourceName, "drawable", getContext().getPackageName());
            image = BitmapFactory.decodeResource(getContext().getResources(), imageResourceId);
            reportImageLoadFinished(true);
            invalidate();
        }
    }

    public void reset() {
        minDimension = getWidth() > getHeight() ? getHeight() : getWidth();
        brushSize = brushSize > 0 ? brushSize : (minDimension / 10.0f);
        brushSize = Math.max(1, Math.min(100, brushSize));
        threshold = threshold > 0 ? threshold : 50;

        loadImage();
        initGrid();
        reportScratchProgress();
        reportScratchState();
    }

    public void initGrid() {
        gridSize = (float) Math.max(Math.min(Math.ceil(minDimension / brushSize), 29), 9);

        grid = new ArrayList();
        for (int x = 0; x < gridSize; x++) {
            grid.add(new ArrayList<Boolean>());
            for (int y = 0; y < gridSize; y++) {
                grid.get(x).add(true);
            }
        }
        clearPointsCounter = 0;
        cleared = false;
        scratchProgress = 0;
    }

    public void updateGrid(int x, int y) {
        float viewWidth = getWidth();
        float viewHeight = getHeight();
        int pointInGridX = Math.round((Math.max(Math.min(x, viewWidth), 0) / viewWidth) * (gridSize - 1.0f));
        int pointInGridY = Math.round((Math.max(Math.min(y, viewHeight), 0) / viewHeight) * (gridSize - 1.0f));
        if (grid.get(pointInGridX).get(pointInGridY) == true) {
            grid.get(pointInGridX).set(pointInGridY, false);
            clearPointsCounter++;
            scratchProgress = ((float) clearPointsCounter) / (gridSize * gridSize) * 100.0f;
            reportScratchProgress();
            if (!cleared && scratchProgress > threshold) {
                cleared = true;
                reportScratchState();
            }
        }
    }

    public void reportImageLoadFinished(boolean success) {
        final Context context = getContext();
        if (context instanceof ReactContext) {
            WritableMap event = Arguments.createMap();
            event.putBoolean("success", success);
            ((ReactContext) context).getJSModule(RCTEventEmitter.class).receiveEvent(getId(),
                    RNTScratchViewManager.EVENT_IMAGE_LOAD, event);
        }
    }

    public void reportTouchState(boolean state) {
        final Context context = getContext();
        if (context instanceof ReactContext) {
            WritableMap event = Arguments.createMap();
            event.putBoolean("touchState", state);
            ((ReactContext) context).getJSModule(RCTEventEmitter.class).receiveEvent(getId(),
                    RNTScratchViewManager.EVENT_TOUCH_STATE_CHANGED, event);
        }
    }

    public void reportScratchProgress() {
        final Context context = getContext();
        if (context instanceof ReactContext) {
            WritableMap event = Arguments.createMap();
            event.putDouble("progressValue", Math.round(scratchProgress * 100.0f) / 100.0);
            ((ReactContext) context).getJSModule(RCTEventEmitter.class).receiveEvent(getId(),
                    RNTScratchViewManager.EVENT_SCRATCH_PROGRESS_CHANGED, event);
        }
    }

    public void reportScratchState() {
        final Context context = getContext();
        if (context instanceof ReactContext) {
            WritableMap event = Arguments.createMap();
            event.putBoolean("isScratchDone", cleared);
            ((ReactContext) context).getJSModule(RCTEventEmitter.class).receiveEvent(getId(),
                    RNTScratchViewManager.EVENT_SCRATCH_DONE, event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!inited && getWidth() > 0) {
            inited = true;
            reset();
        }

        if (!imageTakenFromView) {
            canvas.drawColor(this.placeholderColor != -1 ? this.placeholderColor : Color.TRANSPARENT);
        }

        if (image == null) {
            return;
        }

        if (imageRect == null) {
            int offsetX = 0;
            int offsetY = 0;
            float viewWidth = (float) getWidth();
            float viewHeight = (float) getHeight();
            float imageAspect = (float) image.getWidth() / (float) image.getHeight();
            float viewAspect = viewWidth / viewHeight;
            switch (resizeMode) {
            case "cover":
                if (imageAspect > viewAspect) {
                    offsetX = (int) (((viewHeight * imageAspect) - viewWidth) / 2.0f);
                } else {
                    offsetY = (int) (((viewWidth / imageAspect) - viewHeight) / 2.0f);
                }
                break;
            case "contain":
                if (imageAspect < viewAspect) {
                    offsetX = (int) (((viewHeight * imageAspect) - viewWidth) / 2.0f);
                } else {
                    offsetY = (int) (((viewWidth / imageAspect) - viewHeight) / 2.0f);
                }
                break;
            }
            imageRect = new Rect(-offsetX, -offsetY, getWidth() + offsetX, getHeight() + offsetY);
        }

        canvas.drawBitmap(image, new Rect(0, 0, image.getWidth(), image.getHeight()), imageRect, imagePaint);

        if (path != null) {
            canvas.drawPath(path, pathPaint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
        case MotionEvent.ACTION_DOWN:
            image = createBitmapFromView();
            reportTouchState(true);
            float strokeWidth = brushSize > 0 ? brushSize
                    : ((getHeight() < getWidth() ? getHeight() : getWidth()) / 10f);
            imageRect = new Rect(0, 0, getWidth(), getHeight());
            pathPaint.setStrokeWidth(strokeWidth);
            path = new Path();
            path.moveTo(x, y);
            break;
        case MotionEvent.ACTION_MOVE:
            if (path != null) {
                path.lineTo(x, y);
                updateGrid(x, y);
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            reportTouchState(false);
            image = createBitmapFromView();
            path = null;
            break;
        }
        invalidate();
        return true;
    }

    public Bitmap createBitmapFromView() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        draw(c);
        imageTakenFromView = true;
        return bitmap;
    }
}