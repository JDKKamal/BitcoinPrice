package com.jdkgroup.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.jdkgroup.bitcoinprice.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class AppUtils {
    private static JSONObject jsonobject;
    private static Iterator iterator;

    private static FragmentManager fragmentManager;
    private static FragmentTransaction fragmentTransaction;

    private static AppUtils appUtils;

    public static AppUtils appUtilsInstance() {
        if (appUtils == null) {
            appUtils = new AppUtils();
        }
        return appUtils;
    }

    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message + "", Toast.LENGTH_SHORT);
        TextView textView = toast.getView().findViewById(android.R.id.message);
        if (textView != null) textView.setGravity(Gravity.CENTER);
        toast.show();
    }

    public void showToastById(Context context, int id) {
        Toast toast = Toast.makeText(context, getStringFromId(context, id), Toast.LENGTH_SHORT);
        TextView textView = toast.getView().findViewById(android.R.id.message);
        if (textView != null) textView.setGravity(Gravity.CENTER);
        toast.show();
    }

    private String getStringFromId(Context context, int id) {
        String str = null;
        try {
            str = context.getString(id);
        } catch (Exception e) {
        }
        return str;
    }

    public boolean isInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnectedOrConnecting())) {
            showToast(context, context.getString(R.string.no_internet_message));
            return false;
        }
        return true;
    }

    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /*public static FileUri createImageFile(String prefix) {
        FileUri fileUri = new FileUri();

        File image = null;
        try {
            image = File.createTempFile(prefix + String.valueOf(System.currentTimeMillis()), ".jpg", getWorkingDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image != null) {
            fileUri.setFile(image);
            fileUri.setImageUrl(Uri.parse("file:" + image.getAbsolutePath()));
        }
        return fileUri;
    }*/

    private File getWorkingDirectory() {
        File directory = new File(Environment.getExternalStorageDirectory(), "Mowadcom");
        return createDirectory(directory);
    }

    private File createDirectory(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public String setImage(String imageBaseUrl) {
        // return PocketAccountConstant.IMAGE_BASE_URL.concat(imageBaseUrl);
        return null;
    }

    /* public static RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    } */

    public void startActivity(Context context, Class className) {
        Intent intent = new Intent(context, className);
        context.startActivity(intent);
    }

    public SpannableString timestampToDate(String strTimestamp) {
        try {
            long timestamp = Long.parseLong(strTimestamp) * 1000L;
            DateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");
            Date netDate = (new Date(timestamp));
            String suffix = getDayOfMonthSuffix(netDate.getDate());

            String[] str = sdf.format(netDate).split(" ");
            String strDate = "";
            for (int i = 0; i < str.length; i++) {
                if (i == 0)
                    strDate = str[i] + suffix + " ";
                else if (i == str.length - 1)
                    strDate = strDate + str[i];
                else
                    strDate = strDate + str[i] + " ";
            }

            SpannableString styledString = new SpannableString(strDate);
            styledString.setSpan(new SuperscriptSpan(), 2, 4, 0);
            styledString.setSpan(new RelativeSizeSpan(0.7f), 2, 4, 0);
            styledString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, strDate.length(), 0);
            return styledString;
        } catch (Exception ex) {
            return new SpannableString("");
        }
    }

    static String getDayOfMonthSuffix(final int n) {

        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public String getDateFromTime(long mTimestamp, String mDateFormat) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(mDateFormat);
        dateFormatter.setTimeZone(TimeZone.getDefault());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(mTimestamp);

        return dateFormatter.format(cal.getTime());
    }

    public String getDateString(long miliis, String mDateFormate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(miliis);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(mDateFormate, Locale.ENGLISH);
        return dateFormatter.format(calendar.getTime());
    }

    public String getLongTimestampToDate(long timestamp, String timeFormat) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTimeInMillis(timestamp * 1000L);
        return android.text.format.DateFormat.format(timeFormat, calendar).toString();
    }

    public long getUtC(long millis) {
        return millis + TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
    }

    public long getGMT(long millis) {
        return millis - TimeZone.getDefault().getRawOffset() - TimeZone.getDefault().getDSTSavings();
    }

    public String getDate(long timeStamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("DD MMMM yyyy", Locale.getDefault());
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    public long getDateTimeInMilliseconds(final String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
        Date mDate = sdf.parse(date);
        return mDate.getTime();
    }

    public String getFormatNumber(String val) {
        double d = 0;
        try {
            d = Double.parseDouble(val);
        } catch (Exception e) {
            d = 0.00;
        }
        // return String.format("%.2f",NumberFormat.getNumberInstance(Locale.UK).format(d));
        return NumberFormat.getNumberInstance(Locale.UK).format(d) + "";
    }

    public JSONObject convertMapToJsonObject(final Map map) {
        jsonobject = new JSONObject();
        try {
            iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                jsonobject.put(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
            }
        } catch (Exception ex) { }
        return jsonobject;
    }

    public String getPathFromMediaUri(@NonNull Context context, @NonNull Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public File decreaseImageSize(File file) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;

            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            final int REQUIRED_SIZE = 75;

            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public long getFileSize(File file) {
        long sizeInBytes = file.length();
        return sizeInBytes / (1024 * 1024);
    }

    public int dpToPx(final float dp, final Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public void NavigationBarBackground(final Activity activity, int id) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, id));
        }
    }

    /* public static String bytesToHex(byte[] bytes) {
         if (bytes == null) {
             return "";
         }
         char[] hexChars = new char[bytes.length * 2];
         int v;
         for (int j = 0; j < bytes.length; j++) {
             v = bytes[j] & 0xFF;
             hexChars[j * 2] = hexArray[v >>> 4];
             hexChars[j * 2 + 1] = hexArray[v & 0x0F];
         }
         return new String(hexChars);
     }
 */

    public byte[] stringToBytes(String str) {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }
        return Base64.decode(str, Base64.DEFAULT);
    }

    public byte[] hexToBytes(String hex) {
        if (hex == null) {
            return null;
        }
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public byte[] computeSHA256(byte[] convertme, int offset, int len) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(convertme, offset, len);
            return md.digest();
        } catch (Exception e) {

        }
        return null;
    }

    public long bytesToLong(byte[] bytes) {
        return ((long) bytes[7] << 56) + (((long) bytes[6] & 0xFF) << 48) + (((long) bytes[5] & 0xFF) << 40) + (((long) bytes[4] & 0xFF) << 32)
                + (((long) bytes[3] & 0xFF) << 24) + (((long) bytes[2] & 0xFF) << 16) + (((long) bytes[1] & 0xFF) << 8) + ((long) bytes[0] & 0xFF);
    }

    public String MD5(String md5) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(md5.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public void displayMap(HashMap<String, String> hashMap) {
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //AppUtils.loge("DISPLAY MAP " + PocketAccountConstant.API_PARAMETER + pair.getKey() + pair.getValue());
        }
    }

    //TODO DUPLICATE REMOVE ITEMS
    public <E> List<E> listRemoveDuplicates(List<E> list) {
        Set<E> uniques = new HashSet<E>();
        uniques.addAll(list);
        return new ArrayList<E>(uniques);
    }

    public <E> List<E> linkedHashSetRemoveDuplicates(List<E> list) {
        return new ArrayList<E>(new LinkedHashSet<>(list));
    }

    public boolean isNotEmpty(List list) {
        return list != null && !list.isEmpty();
    }

    public <T> List union(List<T> first, List<T> last) {
        if (isNotEmpty(first) && isNotEmpty(last)) {
            first.addAll(last);
            return first;
        } else if (isNotEmpty(first) && !isNotEmpty(last)) {
            return first;
        }
        return last;
    }

    public boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public String floatFormat2Digit(String str) {
        return String.format("%.02f", str);
    }

    public float discount(float rs, float tax) {
        return ((rs * tax) / 100);
    }

    public float roundValue(float value) {
        return Math.round(value);
    }

    public boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager().getPackageInfo(pckName, 0);
            if (pckInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return false;
    }

    public void uninstallApk(Context context, String packageName) {
        if (isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    public void requestParam(HashMap<String, String> hashMap) {
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Logging.i(pair.getKey() + " - " + pair.getValue());
        }
    }

    private String stringFromByte(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while (true) {
                String str = bufferedReader.readLine();
                if (str == null)
                    break;
                stringBuilder.append(str);
            }
        } catch (Exception localIOException) {
            localIOException.printStackTrace();
        }
        return inputStream.toString();
    }
}
