package com.nghiepnguyen.survey;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nghiepnguyen.survey.model.SaveAnswerModel;

/**
 * Created by nghiep on 6/3/16.
 */
public class UserInfoDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "CancelManagementDialog";
    private Context mContext;
    private ICallBackSaveUserInfo iCallBackSaveUserInfo;

    private View mDialogView;
    private TextView mFullNameTextView;
    private TextView mNumberIdTextView;
    private TextView mPhoneNumberTextView;
    private TextView mAddressTextView;
    private TextView mEmailTextView;
    private Button mCancelButton;
    private Button mOkButton;
    private Animation mAnimationIn;
    private Animation mAnimationOut;

    private SaveAnswerModel saveAnswerModel;

    public UserInfoDialog(Context context, int themeResId, ICallBackSaveUserInfo iCallBackSaveUserInfo) {
        super(context, themeResId);
        this.mContext = context;
        this.iCallBackSaveUserInfo = iCallBackSaveUserInfo;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.dialog_user_info);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.cl_float_transparent)));
        initComponent();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (displaymetrics.widthPixels * 0.9);
        int height = (int) (displaymetrics.widthPixels * 0.9);
        FrameLayout.LayoutParams layoutPara = new FrameLayout.LayoutParams(width, height);
        layoutPara.gravity = Gravity.CENTER;
        mDialogView.setLayoutParams(layoutPara);

        isEnableAnimation();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAnimationIn != null)
            mDialogView.startAnimation(mAnimationIn);

    }

    @Override
    public void dismiss() {
        if (mAnimationOut != null)
            mDialogView.startAnimation(mAnimationOut);
        else
            UserInfoDialog.super.dismiss();
    }

    private void initComponent() {
        mDialogView = findViewById(R.id.dialog_user_info_view);
        mFullNameTextView = (TextView) findViewById(R.id.dialog_user_info_full_name_edittext);
        mNumberIdTextView = (TextView) findViewById(R.id.dialog_user_info_number_id_edittext);
        mPhoneNumberTextView = (TextView) findViewById(R.id.dialog_user_info_phone_number_edittext);
        mAddressTextView = (TextView) findViewById(R.id.dialog_user_info_address_edittext);
        mEmailTextView = (TextView) findViewById(R.id.dialog_user_info_email_edittext);

        mCancelButton = (Button) findViewById(R.id.dialog_user_info_cancel_button);
        mOkButton = (Button) findViewById(R.id.dialog_user_info_ok_button);

        mCancelButton.setOnClickListener(this);
        mOkButton.setOnClickListener(this);
    }

    private void isEnableAnimation() {
        mAnimationIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_dialog_in);
        mAnimationOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_dialog_out);
        mAnimationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        UserInfoDialog.super.dismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void setICallBack(ICallBackSaveUserInfo iCallBack) {
        this.iCallBackSaveUserInfo = iCallBack;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.dialog_user_info_cancel_button:
                UserInfoDialog.this.dismiss();
                break;
            case R.id.dialog_user_info_ok_button:
                saveAnswerModel = new SaveAnswerModel(
                        mFullNameTextView.getText().toString(),
                        mNumberIdTextView.getText().toString(),
                        mPhoneNumberTextView.getText().toString(),
                        mEmailTextView.getText().toString(),
                        mAddressTextView.getText().toString());

                UserInfoDialog.this.dismiss();
                if (iCallBackSaveUserInfo != null)
                    iCallBackSaveUserInfo.onSaveUserInfo(saveAnswerModel);
                break;
        }
    }

    public interface ICallBackSaveUserInfo {
        public void onSaveUserInfo(SaveAnswerModel saveAnswerModel);
    }
}
