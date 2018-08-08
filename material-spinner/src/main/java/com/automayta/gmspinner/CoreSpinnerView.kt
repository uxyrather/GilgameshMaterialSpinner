package com.automayta.gmspinner


import android.content.Context
import android.os.Build
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.PopupWindow
import java.util.*

/**
 * Created by Uzair.Mohammad on 9/21/2017.
 */

class CoreSpinnerView : FrameLayout, View.OnTouchListener, CoreSpinnerPopDownWindowRVAdapter.ItemSelected {

    internal var dropDownItems: HashMap<Any, Any>? = null
    internal var onItemSelectionChangeListener: OnItemSelectionChangeListener? = null
    private var editTextSpinner: EditText? = null
    private var textInputLayoutSpinner: TextInputLayout? = null
    private var linearLayoutSpinner: FrameLayout? = null
    private var hint: String? = null
    private var prompt: String? = null
    private var mPopupWindow: PopupWindow? = null
    var defaultPosition: Any? = null
        set(defaultPosition) {
            field = defaultPosition
            setSelection(defaultPosition)
        }
    private var ids: Int = 0
    private var parentWidth: Int = 0

    private val selection: HashMap<Any, Any>?
        get() {
            if (editTextSpinner!!.tag != null) {
                val hashMap = HashMap<Any, Any>()
                hashMap[editTextSpinner!!.tag] = editTextSpinner!!.text
                return hashMap
            } else {
                return null
            }
        }

    /**
     * This represents the key associated with the value, remember
     * it might be same as the key we have for this spinner item but not always
     *
     * @return
     */
    val key: Any
        get() {
            val hashMapKeyValue = selection
            return hashMapKeyValue!!.keys.toTypedArray()[0]
        }

    /**
     * This method returns the value that user can view as selected on spinner
     *
     * @return value
     */
    val value: Any
        get() {
            val hashMapKeyValue = selection
            return hashMapKeyValue!![key].toString()
        }


    fun disable() {
        editTextSpinner!!.isEnabled = false
        textInputLayoutSpinner!!.isEnabled = false
    }

    fun enable() {
        editTextSpinner!!.isEnabled = true
        textInputLayoutSpinner!!.isEnabled = true
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, context)
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, context)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs, context)

    }

    fun setAdapter(dropDownItems: Array<String>, defaultPosition: Any) {
        val hashMap = HashMap<Any, Any>()
        var i = 0
        for (value in dropDownItems) {
            hashMap[i++] = value
        }
        this.dropDownItems = hashMap
        this.defaultPosition = defaultPosition
    }

    fun setAdapter(hashMap: HashMap<Any, Any>, defaultPosition: Any) {
        this.dropDownItems = hashMap
        this.defaultPosition = defaultPosition
    }

    private fun init(attrs: AttributeSet?, context: Context) {

        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.core_spinner, this)

        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CoreSpinnerView,
                0, 0)
        try {
            hint = a.getString(R.styleable.CoreSpinnerView_spinnerHint)
            prompt = a.getString(R.styleable.CoreSpinnerView_spinnerPrompt)
            //            defaultPosition = a.getInt(R.styleable.CoreSpinnerView_spinnerDefaultPosition, 0);
        } finally {
            a.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ids = this.id
        this.id = ids
        editTextSpinner = findViewById<View>(R.id.editTextSpinner) as EditText
        textInputLayoutSpinner = findViewById<View>(R.id.textInputLayoutSpinner) as TextInputLayout
        linearLayoutSpinner = findViewById<View>(R.id.linearLayoutSpinner) as FrameLayout
        setDefaultHint()
        editTextSpinner!!.setOnTouchListener(this)
    }

    private fun setDefaultHint() {
        textInputLayoutSpinner!!.hint = hint
        editTextSpinner!!.setText(prompt)

    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            KeyEvent.ACTION_UP -> {
                editTextSpinner!!.requestFocus()
                if (dropDownItems != null && dropDownItems!!.size > 0) {
                    showDropDown()
                }
            }
        }
        return true
    }

    private fun showDropDown() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


        parentWidth = textInputLayoutSpinner!!.width + 26


        mPopupWindow = PopupWindow(inflater.inflate(R.layout.core_spinner_popup, null),
                parentWidth,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        mPopupWindow!!.isOutsideTouchable
        mPopupWindow!!.elevation = 12f
        mPopupWindow!!.isOutsideTouchable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPopupWindow!!.overlapAnchor = true
        }

        mPopupWindow!!.setIgnoreCheekPress()
        mPopupWindow!!.showAsDropDown(rootView.findViewById(id), 16, 16)

        val recyclerViewCoreSpinner = mPopupWindow!!.contentView.findViewById<View>(R.id.recyclerViewCoreSpinner) as RecyclerView
        recyclerViewCoreSpinner.layoutManager = LinearLayoutManager(context)
        recyclerViewCoreSpinner.adapter = CoreSpinnerPopDownWindowRVAdapter(dropDownItems!!, context, this)
    }

    override fun OnDropDownItemSelectedListener(key: Any?, value: String) {
        mPopupWindow!!.dismiss()
        setSelection(key)
    }

    fun setSelection(position: Any?) {
        if (dropDownItems != null && dropDownItems!!.size > 0) {
            var value = ""
            var key = this.defaultPosition
            for (mItem in dropDownItems!!.entries) {
                if (position is Int) {
                    key = Integer.valueOf((mItem as MutableMap.MutableEntry<*, *>).key.toString())
                    if (key === position as Int?) {
                        value = (mItem as MutableMap.MutableEntry<*, *>).value.toString()
                        editTextSpinner!!.setText(value)
                        editTextSpinner!!.tag = key
                        break

                    }
                } else if (position is String) {
                    key = (mItem as MutableMap.MutableEntry<*, *>).key.toString()
                    if ((key).equals((position as String?)!!, ignoreCase = true)) {
                        value = (mItem as MutableMap.MutableEntry<*, *>).value.toString()
                        editTextSpinner!!.setText(value)
                        editTextSpinner!!.tag = key
                        break

                    }
                }

            }
            if (onItemSelectionChangeListener != null)
                onItemSelectionChangeListener!!.OnItemSelectionChangeListener(key!!, value)
        }
    }

    fun setOnDropDownItemSelectedListener(listener: OnItemSelectionChangeListener) {
        onItemSelectionChangeListener = listener
    }


    interface OnItemSelectionChangeListener {
        fun OnItemSelectionChangeListener(position: Any, fieldValue: String)
    }
}
