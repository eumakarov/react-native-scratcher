import React, { Component } from 'react';
import { StyleSheet, Animated, requireNativeComponent } from 'react-native';

const RNTScratchView = requireNativeComponent('RNTScratchView', ScratchView);

const AnimatedScratchView = RNTScratchView && Animated.createAnimatedComponent(RNTScratchView);

class ScratchView extends Component {
    constructor(props) {
        super(props);

        this.state = {
            animatedValue: new Animated.Value(1),
            isScratchDone: false,
            visible: true,
        };

        this.scratchOpacity = {
            opacity: this.state.animatedValue.interpolate({
                inputRange: [0, 1],
                outputRange: [1, 0],
            }),
        };
    }

    _onImageLoadFinished = (e) => {
        const { id, onImageLoadFinished } = this.props;
        const success = JSON.parse(e.nativeEvent.success);
        onImageLoadFinished && onImageLoadFinished({ id, success });
    }

    _onTouchStateChanged = (e) => {
        const { id, onTouchStateChanged } = this.props;
        const touchState = JSON.parse(e.nativeEvent.touchState);
        const { isScratchDone } = this.state;

        onTouchStateChanged && onTouchStateChanged({ id, touchState });
        if (!touchState && isScratchDone && !this.hideTimeout && this.props.fadeOut !== false) {
            const that = this;
            this.hideTimeout = setTimeout(() => {
                that.setState({ visible: false });
            }, 300);
        }
    }

    _onScratchProgressChanged = (e) => {
        const { id, onScratchProgressChanged } = this.props;
        const { progressValue } = e.nativeEvent;
        onScratchProgressChanged && onScratchProgressChanged({ id, value: parseFloat(progressValue) });
    }

    _onScratchDone = (e) => {
        const { id, onScratchDone } = this.props;
        const isScratchDone = JSON.parse(e.nativeEvent.isScratchDone);
        if (isScratchDone) {
            this.setState({
                isScratchDone,
            }, () => {
                this.fadeOut(() => {
                    onScratchDone && onScratchDone({ id, isScratchDone });
                });
            });
        }
    }

    fadeOut(postAction) {
        if (this.props.fadeOut === false) {
            postAction && postAction();
        } else {
            this.state.animatedValue.setValue(1);
            Animated.timing(this.state.animatedValue, {
                toValue: 0,
                duration: 300,
                useNativeDriver: true,
            }).start(postAction);
        }
    }

    render() {
        if (AnimatedScratchView && this.state.visible) {
            return (
                <AnimatedScratchView
                    {...this.props}
                    style={[styles.container, { opacity: this.state.animatedValue }]}
                    onImageLoadFinished={this._onImageLoadFinished}
                    onTouchStateChanged={this._onTouchStateChanged}
                    onScratchProgressChanged={this._onScratchProgressChanged}
                    onScratchDone={this._onScratchDone}
                />
            );
        }
        return null;
    }
}


const styles = StyleSheet.create({
    container: {
        position: 'absolute',
        width: '100%',
        height: '100%'
    },
});

export default ScratchView