package sword.qrcode.cameraconfig;

import android.hardware.Camera;

public class OpenCamera {
    private final Camera camera;
    private final int facing;
    private final int orientation;

    public OpenCamera(Camera camera, int facing, int orientation){
        this.camera = camera;
        this.facing = facing;
        this.orientation = orientation;
    }

    public Camera getCamera() {
        return camera;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getFacing() {
        return facing;
    }
}
