package com.como.RNTScratchView;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class RNTScratchViewManager extends SimpleViewManager<ScratchView> {
    public static final String REACT_CLASS = "RNTScratchView";
    public static final String EVENT_IMAGE_LOAD = "onImageLoadFinished";
    public static final String EVENT_TOUCH_STATE_CHANGED = "onTouchStateChanged";
    public static final String EVENT_SCRATCH_PROGRESS_CHANGED = "onScratchProgressChanged";
    public static final String EVENT_SCRATCH_DONE = "onScratchDone";

    @ReactProp(name = "placeholderColor")
    public void setPlaceholderColor(final ScratchView scratchView, @Nullable String placeholderColor) {
        if (scratchView != null) {
            scratchView.setPlaceholderColor(placeholderColor);
        }
    }

    @ReactProp(name = "threshold")
    public void setThreshold(final ScratchView scratchView, float threshold) {
        if (scratchView != null) {
            scratchView.setThreshold(threshold);
        }
    }

    @ReactProp(name = "brushSize")
    public void setBrushSize(final ScratchView scratchView, float brushSize) {
        if (scratchView != null) {
            scratchView.setBrushSize(brushSize);
        }
    }

    @ReactProp(name = "imageUrl")
    public void setImageUrl(final ScratchView scratchView, @Nullable String imageUrl) {
        if (scratchView != null) {
            scratchView.setImageUrl(imageUrl);
        }
    }

    @ReactProp(name = "resourceName")
    public void setResourceName(final ScratchView scratchView, @Nullable String resourceName) {
        if (scratchView != null) {
            scratchView.setResourceName(resourceName);
        }
    }

    @ReactProp(name = "localImageName") // deprecated
    public void setLocalImageName(final ScratchView scratchView, @Nullable String localImageName) {
        if (scratchView != null) {
            scratchView.setResourceName(localImageName);
        }
    }

    @ReactProp(name = "resizeMode")
    public void setResizeMode(final ScratchView scratchView, @Nullable String resizeMode) {
        if (scratchView != null) {
            scratchView.setResizeMode(resizeMode);
        }
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public ScratchView createViewInstance(ThemedReactContext context) {
        return new ScratchView(context);
    }

    @javax.annotation.Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("reset", 0);
    }

    @Override
    public void receiveCommand(ScratchView view, int commandId, @javax.annotation.Nullable ReadableArray args) {
        super.receiveCommand(view, commandId, args);
        if (commandId == 0) {
            view.reset();
        }
    }

    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put(EVENT_IMAGE_LOAD,
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", EVENT_IMAGE_LOAD)))
                .put(EVENT_TOUCH_STATE_CHANGED,
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", EVENT_TOUCH_STATE_CHANGED)))
                .put(EVENT_SCRATCH_PROGRESS_CHANGED,
                        MapBuilder.of("phasedRegistrationNames",
                                MapBuilder.of("bubbled", EVENT_SCRATCH_PROGRESS_CHANGED)))
                .put(EVENT_SCRATCH_DONE,
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", EVENT_SCRATCH_DONE)))
                .build();
    }
}