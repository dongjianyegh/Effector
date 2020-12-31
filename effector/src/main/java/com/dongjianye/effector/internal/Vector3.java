package com.dongjianye.effector.internal;

import androidx.annotation.NonNull;

/**
 * @author dongjianye on 12/31/20
 */
public class Vector3 {

    private float x;

    private float y;

    private float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final void scale(Vector3 scale) {
        setX(getX() * scale.getX());
        setY(getY() * scale.getY());
        setZ(getZ() * scale.getZ());
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final float getZ() {
        return this.z;
    }

    public final void setX(float x) {
        this.x = x;
    }

    public final void setY(float y) {
        this.y = y;
    }

    public final void setZ(float z) {
        this.z = z;
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder("[x:").append(x)
                .append(", y:").append(y)
                .append(", z:").append(z)
                .append("]").toString();
    }
}