package EditImage;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by huynh on 12/12/2017.
 */

public class XuLyAnh
{
    public XuLyAnh() {
    }

    public static byte[] layMangByteTuBitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }

    //Hàm dùng để xử lý hình ảnh nhưng vẫn giữ chất lượng ảnh như ban đầu
    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
