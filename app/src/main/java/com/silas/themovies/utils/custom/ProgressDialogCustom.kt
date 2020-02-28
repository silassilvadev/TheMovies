package com.silas.themovies.utils.custom

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.silas.themovies.R
import java.util.*

/**
 * Class that creates a transparent progress bar while the user waits for some action
 *
 * @property TAG progress bar identifier
 * @property instance last instance called class
 * @property show attribute that saves progress bar state
 *
 *@author silas.silva 23/02/2020
 * */

class ProgressDialogCustom : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return AlertDialog.Builder(Objects.requireNonNull<FragmentActivity>(activity))
                .setView(activity
                        ?.layoutInflater
                        ?.inflate(R.layout.custom_progress_dialog, activity?.contentScene?.sceneRoot))
                .create()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Objects.requireNonNull<Window>(dialog?.window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Method must be called to start progress
     *
     * @param manager used to inflate the dialog in the view.
     * */
    fun show(manager: FragmentManager) {
        try {
            if (show || dialog != null && dialog!!.isShowing) return
            show = true
            super.show(manager, TAG)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Method must be called to finalize progress
     * */
    override fun dismiss() {
        try {
            show = false
            if (!isHidden) super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "tagLoading"
        private var progressDialogCustom: ProgressDialogCustom? = null
        private var show = false

        val instance: ProgressDialogCustom
            @Synchronized get() {
                if (progressDialogCustom == null) {
                    progressDialogCustom = ProgressDialogCustom()
                }
                return progressDialogCustom!!
            }
    }
}