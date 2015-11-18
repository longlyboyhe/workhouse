package android.com.fastandroid.ui.views;

import android.Manifest;
import android.com.fastandroid.R;
import android.com.fastandroid.permission.Func;
import android.com.fastandroid.permission.Func2;
import android.com.fastandroid.permission.PermissionUtil;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class TestPermissionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_CONTACTS = 1;
    private static final int REQUEST_CODE_STORAGE = 2;
    private static final int REQUEST_CODE_BOTH = 3;

    private PermissionUtil.PermissionRequestObject mStoragePermissionRequest;
    private PermissionUtil.PermissionRequestObject mContactsPermissionRequest;
    private PermissionUtil.PermissionRequestObject mBothPermissionRequest;

    private TextView mStatus;
    private TextView mBoth;
    private TextView mStorage;
    private TextView mContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_permission);
        mStatus = (TextView) findViewById(R.id.state);
        mBoth = (TextView) findViewById(R.id.both);
        mStorage = (TextView) findViewById(R.id.storage);
        mContacts = (TextView) findViewById(R.id.contacts);
        mBoth.setOnClickListener(this);
    }

    public void onAskBothPermissionsClick() {
        mBothPermissionRequest =
                PermissionUtil.with(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS).onResult(
                        new Func2() {
                            @Override
                            protected void call(int requestCode, String[] permissions, int[] grantResults) {
                                for (int i = 0; i < permissions.length; i++) {
                                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                                        doOnPermissionGranted(permissions[i]);
                                    else doOnPermissionDenied(permissions[i]);
                                }
                            }
                        }).ask(REQUEST_CODE_BOTH);

    }


    public void onAskForStoragePermissionClick() {
        mStoragePermissionRequest = PermissionUtil.with(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).onAllGranted(
                new Func() {
                    @Override
                    protected void call() {
                        doOnPermissionGranted("Storage");
                    }
                }).onAnyDenied(
                new Func() {
                    @Override
                    protected void call() {
                        doOnPermissionDenied("Storage");
                    }
                }).ask(REQUEST_CODE_STORAGE);

    }

    public void onAskForContactsPermissionClick() {
        mContactsPermissionRequest = PermissionUtil.with(this).request(
                Manifest.permission.WRITE_CONTACTS);
        mContactsPermissionRequest.onAllGranted(
                new Func() {
                    @Override
                    protected void call() {
                        doOnPermissionGranted("Contacts");
                    }
                }).onAnyDenied(
                new Func() {
                    @Override
                    protected void call() {
                        doOnPermissionDenied("Contacts");
                    }
                }).ask(REQUEST_CODE_CONTACTS);
    }

    private void updateStatus(String s) {
        mStatus.setText(String.format("> %s\n", s) + mStatus.getText().toString());
    }

    private void doOnPermissionDenied(String permission) {
        updateStatus(permission + " Permission Denied or is on \"Do Not SHow Again\"");
    }

    private void doOnPermissionGranted(String permission) {
        updateStatus(permission + " Permission Granted");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mStoragePermissionRequest != null)
            mStoragePermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (mContactsPermissionRequest != null)
            mContactsPermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (mBothPermissionRequest != null)
            mBothPermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.both:
                onAskBothPermissionsClick();
                break;
            case R.id.contacts:
                onAskForContactsPermissionClick();
                break;
            case R.id.storage:
                onAskForContactsPermissionClick();
                break;
        }
    }
}
