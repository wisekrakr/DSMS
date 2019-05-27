package com.wisekrakr.firstgame.input;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.GLVersion;

public abstract class CursorAim implements com.badlogic.gdx.graphics.Cursor, Graphics {

    private Cursor cursor;

    public CursorAim() {
        Pixmap pixmap = new Pixmap(new FileHandle("texture/badlogic.jpg"));
        cursor = newCursor(pixmap, pixmap.getWidth() / 2, pixmap.getHeight() / 2);
        setCursor(cursor);

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isGL30Available() {
        return false;
    }

    @Override
    public GL20 getGL20() {
        return null;
    }

    @Override
    public GL30 getGL30() {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getBackBufferWidth() {
        return 0;
    }

    @Override
    public int getBackBufferHeight() {
        return 0;
    }

    @Override
    public long getFrameId() {
        return 0;
    }

    @Override
    public float getDeltaTime() {
        return 0;
    }

    @Override
    public float getRawDeltaTime() {
        return 0;
    }

    @Override
    public int getFramesPerSecond() {
        return 0;
    }

    @Override
    public GraphicsType getType() {
        return null;
    }

    @Override
    public GLVersion getGLVersion() {
        return null;
    }

    @Override
    public float getPpiX() {
        return 0;
    }

    @Override
    public float getPpiY() {
        return 0;
    }

    @Override
    public float getPpcX() {
        return 0;
    }

    @Override
    public float getPpcY() {
        return 0;
    }

    @Override
    public float getDensity() {
        return 0;
    }

    @Override
    public boolean supportsDisplayModeChange() {
        return false;
    }

    @Override
    public Monitor getPrimaryMonitor() {
        return null;
    }

    @Override
    public Monitor getMonitor() {
        return null;
    }

    @Override
    public Monitor[] getMonitors() {
        return new Monitor[0];
    }

    @Override
    public DisplayMode[] getDisplayModes() {
        return new DisplayMode[0];
    }

    @Override
    public DisplayMode[] getDisplayModes(Monitor monitor) {
        return new DisplayMode[0];
    }

    @Override
    public DisplayMode getDisplayMode() {
        return null;
    }

    @Override
    public DisplayMode getDisplayMode(Monitor monitor) {
        return null;
    }

    @Override
    public boolean setFullscreenMode(DisplayMode displayMode) {
        return false;
    }

    @Override
    public boolean setWindowedMode(int width, int height) {
        return false;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setUndecorated(boolean undecorated) {

    }

    @Override
    public void setResizable(boolean resizable) {

    }

    @Override
    public void setVSync(boolean vsync) {

    }

    @Override
    public BufferFormat getBufferFormat() {
        return null;
    }

    @Override
    public boolean supportsExtension(String extension) {
        return false;
    }

    @Override
    public void setContinuousRendering(boolean isContinuous) {

    }

    @Override
    public boolean isContinuousRendering() {
        return false;
    }

    @Override
    public void requestRendering() {

    }

    @Override
    public boolean isFullscreen() {
        return false;
    }

    @Override
    public com.badlogic.gdx.graphics.Cursor newCursor(Pixmap pixmap, int xHotspot, int yHotspot) {
        return null;
    }

    @Override
    public void setCursor(com.badlogic.gdx.graphics.Cursor cursor) {

    }

    @Override
    public void setSystemCursor(SystemCursor systemCursor) {

    }
}
