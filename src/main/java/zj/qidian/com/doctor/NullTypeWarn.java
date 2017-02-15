package zj.qidian.com.doctor;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by xdsm on 2016/12/20.
 */

public class NullTypeWarn extends AlertDialog {
    protected NullTypeWarn(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nulltypewarn);
    }
}
