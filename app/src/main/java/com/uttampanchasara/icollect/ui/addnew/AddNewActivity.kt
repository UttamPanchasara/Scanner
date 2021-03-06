package com.uttampanchasara.icollect.ui.addnew

import android.app.DatePickerDialog
import android.content.Intent
import androidx.core.widget.toast
import com.uttampanchasara.icollect.R
import com.uttampanchasara.icollect.di.component.ActivityComponent
import com.uttampanchasara.icollect.ui.base.BaseActivity
import com.uttampanchasara.icollect.ui.scanner.ScannerActivity
import kotlinx.android.synthetic.main.activity_add_new.*
import java.util.*
import javax.inject.Inject

/**
 * @since 11/26/2018
 */
class AddNewActivity : BaseActivity(), AddNewView {

    companion object {
        val REQUEST_CODE = 1110
    }

    override fun getLayout(): Int {
        return R.layout.activity_add_new
    }

    override fun injectComponents(mActivityComponent: ActivityComponent) {
        mActivityComponent.inject(this)
    }

    @Inject
    lateinit var mViewModel: AddNewViewModel

    var mSelectedDate: Long = 0

    override fun setUp() {
        mViewModel.onAttachView(this)
        setToolbar(toolbar, "Add New", true)
        btnSave.setOnClickListener {
            saveRecord()
        }

        btnScan.setOnClickListener {
            startScanner()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.onDetachView()
    }

    private fun startScanner() {
        startActivityForResult(Intent(this, ScannerActivity::class.java), REQUEST_CODE)
    }

    private fun saveRecord() {
        val code = edtCode.text.toString()
        val name = edtName.text.toString()
        val number = edtNumber.text.toString()
        val address = edtAddress.text.toString()

        when {
            code.isEmpty() -> {
                toast(getString(R.string.please_enter_code))
            }
            name.isEmpty() -> {
                toast(getString(R.string.please_enter_name))
            }
            else -> {
                mViewModel.saveRecord(code, name, number, address)
            }
        }
    }

    override fun onRecordInserted() {
        toast("Data Saved!")
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == ScannerActivity.RESULT_CODE) {
            edtCode.setText(data?.getStringExtra(ScannerActivity.DATA))
        }
    }

    private fun showDatePicker() {
        // Get Current Date
        val c = Calendar.getInstance()
        val fromYear = c.get(Calendar.YEAR)
        val fromMonth = c.get(Calendar.MONTH)
        val fromDay = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            val calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
            mSelectedDate = calendar.timeInMillis

        }, fromYear, fromMonth, fromDay)
        dialog.show()
    }
}