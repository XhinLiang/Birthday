package io.github.xhinliang.mdpreference;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;

import com.rey.material.dialog.Dialog;

/**
 * Created by xhinliang on 16-2-23.
 * DialogPreference
 */
public abstract class DialogPreference extends Preference {

    protected CharSequence mDialogTitle;
    protected CharSequence mDialogMessage;
    protected CharSequence mPositiveButtonText;
    protected CharSequence mNegativeButtonText;

    protected Dialog mDialog;

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mDialogTitle = getTitle();
        mPositiveButtonText = context.getText(R.string.confirm);
        mNegativeButtonText = context.getText(R.string.cancel);
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogPreference(Context context) {
        super(context);
        init(context);
    }

    /**
     * Sets the title of the dialog. This will be shown on subsequent dialogs.
     *
     * @param dialogTitle The title.
     */
    public void setDialogTitle(CharSequence dialogTitle) {
        mDialogTitle = dialogTitle;
    }

    /**
     * @param dialogTitleResId The dialog title as a resource.
     * @see #setDialogTitle(CharSequence)
     */
    public void setDialogTitle(int dialogTitleResId) {
        setDialogTitle(getContext().getString(dialogTitleResId));
    }

    /**
     * Returns the title to be shown on subsequent dialogs.
     *
     * @return The title.
     */
    public CharSequence getDialogTitle() {
        return mDialogTitle;
    }

    /**
     * Sets the message of the dialog. This will be shown on subsequent dialogs.
     * <p/>
     * This message forms the content View of the dialog and conflicts with
     * list-based dialogs, for example. If setting a custom View on a dialog via
     * {@link android.R.id#message} and it will be populated with this message.
     *
     * @param dialogMessage The message.
     */
    public void setDialogMessage(CharSequence dialogMessage) {
        mDialogMessage = dialogMessage;
    }

    /**
     * Returns the message to be shown on subsequent dialogs.
     *
     * @return The message.
     */
    public CharSequence getDialogMessage() {
        return mDialogMessage;
    }

    /**
     * Sets the text of the positive button of the dialog. This will be shown on
     * subsequent dialogs.
     *
     * @param positiveButtonText The text of the positive button.
     */
    public void setPositiveButtonText(CharSequence positiveButtonText) {
        mPositiveButtonText = positiveButtonText;
    }

    /**
     * @param positiveButtonTextResId The positive button text as a resource.
     * @see #setPositiveButtonText(CharSequence)
     */
    public void setPositiveButtonText(@StringRes int positiveButtonTextResId) {
        setPositiveButtonText(getContext().getString(positiveButtonTextResId));
    }

    /**
     * Sets the text of the negative button of the dialog. This will be shown on
     * subsequent dialogs.
     *
     * @param negativeButtonText The text of the negative button.
     */
    public void setNegativeButtonText(CharSequence negativeButtonText) {
        mNegativeButtonText = negativeButtonText;
    }

    /**
     * @param negativeButtonTextResId The negative button text as a resource.
     * @see #setNegativeButtonText(CharSequence)
     */
    public void setNegativeButtonText(@StringRes int negativeButtonTextResId) {
        setNegativeButtonText(getContext().getString(negativeButtonTextResId));
    }


    @Override
    protected void onClick() {
        if (mDialog != null && mDialog.isShowing()) return;
        showDialog(null);
    }

    /**
     * Shows the dialog associated with this Preference. This is normally initiated
     * automatically on clicking on the preference. Call this method if you need to
     * show the dialog on some other event.
     *
     * @param state Optional instance state to restore on the dialog
     */
    protected void showDialog(Bundle state) {

    }

    /**
     * Returns whether the preference needs to display a soft input method when the dialog
     * is displayed. Default is false. Subclasses should override this method if they need
     * the soft input method brought up automatically.
     */
    protected boolean needInputMethod() {
        return false;
    }


    /**
     * Gets the dialog that is shown by this preference.
     *
     * @return The dialog, or null if a dialog is not being shown.
     */
    public Dialog getDialog() {
        return mDialog;
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mDialog == null || !mDialog.isShowing()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = mDialog.onSaveInstanceState();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }

    private static class SavedState extends BaseSavedState {
        boolean isDialogShowing;
        Bundle dialogBundle;

        public SavedState(Parcel source) {
            super(source);
            isDialogShowing = source.readInt() == 1;
            dialogBundle = source.readBundle();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isDialogShowing ? 1 : 0);
            dest.writeBundle(dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
