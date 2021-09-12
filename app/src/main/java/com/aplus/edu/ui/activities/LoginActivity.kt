package com.aplus.edu.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aplus.edu.R
import com.aplus.edu.api.manager.APIManager
import com.aplus.edu.api.response.*
import com.aplus.edu.api.services.TuitionApiService
import com.aplus.edu.preference.UserInfo
import com.aplus.edu.ui.activities.BaseActivity.Companion.dismissProgress
import com.aplus.edu.ui.activities.BaseActivity.Companion.showProgress
import kotlinx.android.synthetic.main.login.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    var loginUserName = ""
    var loginPaassword = ""
    //reg fields
    var reg_studentname1=""
    var spinnerView2_val1=""
    var reg_mobileno1=""
    var reg_email1=""
    var reg_pwd1=""
    var classes_data: List<ClassItem?>? = null
    val classnames:ArrayList<String>?= ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        initViews()
        getClasses()

    }

    fun initViews()
    {
        //username.setText("gopika@gmail.com")
        //password.setText("123456")
        log_in.visibility= View.VISIBLE
        register_main.visibility= View.GONE
        txt_log_in .setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_white_right));
        txt_log_in.setTextColor(resources.getColor(R.color.black_text))
        txt_log_in.setOnClickListener {
            txt_register.setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_gradient_bg));
            log_in.visibility= View.VISIBLE
            register_main.visibility= View.GONE
            txt_log_in.setTextColor(resources.getColor(R.color.black_text))
            txt_register.setTextColor(resources.getColor(R.color.white))
            txt_log_in .setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_white_right));
        }

        txt_register.setOnClickListener {
            txt_log_in.setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_gradient_bg));
            bottom_imv.visibility=View.GONE
            register_main.visibility= View.VISIBLE
            log_in.visibility= View.GONE
            txt_log_in.setTextColor(resources.getColor(R.color.white))
            txt_register.setTextColor(resources.getColor(R.color.black_text))
            txt_register .setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_white_left));
        }

        //

        btnLogin.setOnClickListener {

            if (isAllInputValidLogin()){
                processLogin()
            }else{
                Toast.makeText(applicationContext, "Please enter email and password" , Toast.LENGTH_LONG).show()
//toast here
            }
        }

        btnRegister.setOnClickListener {

            if (isAllInputValidRegister()){
                processRegister()
            }else{
                Toast.makeText(applicationContext, "Please fill all fields" , Toast.LENGTH_LONG).show()
//toast here
            }

        }

//forgotpass
        forgotpassword.setOnClickListener {
            //show dialog
            forgotpassdialog()

        }

    }

    private fun processLogin(){
        lifecycleScope.launch {
            showProgress(this@LoginActivity, "Processing..")
            val response =  APIManager.call<TuitionApiService, Response<LoginResp>> {
                loginUser(loginJsonRequest())
            }
            try {
                if (response != null && response.body()!!.error==false){
                    dismissProgress()
                    //home page
                    startActivity(Intent(applicationContext, Home::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    //add to pref
                    saveUserInfo(response.body()!!.classid,response.body()!!.data)
                    finish()
                    Toast.makeText(this@LoginActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this@LoginActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    dismissProgress()


                }
            } catch (e: Exception) {
                dismissProgress()
                e.printStackTrace()

            }
        }
    }

    private fun saveUserInfo(classid: Int?, data: Int?){
        UserInfo().uid = data.toString()
        UserInfo().classid = classid.toString()
    }


    private fun isAllInputValidLogin(): Boolean{
        if (username.text.toString().isNotEmpty()&&
            password.text.toString().isNotEmpty() &&
            isValidEmail(username.text.toString())){
            loginUserName = username.text.toString()
            loginPaassword = password.text.toString()
            return true
        }
        return false
    }

    private fun loginJsonRequest() : RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("email", loginUserName)
            json.put("password", loginPaassword)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    //register

    private fun isAllInputValidRegister(): Boolean{
        if (reg_studentname.text.toString().isNotEmpty()&&
            //spinner check
            spinnerView2.selectedItem.toString().trim() != "Select Class"
            && reg_mobileno.text.toString().isNotEmpty()
            && reg_email.text.toString().isNotEmpty()
            && reg_pwd.text.toString().isNotEmpty()&&
            isValidEmail(reg_email.text.toString())
        ){
            reg_studentname1 = reg_studentname.text.toString()
            reg_mobileno1 = reg_mobileno.text.toString()
            reg_email1 = reg_email.text.toString()
            reg_pwd1 = reg_pwd.text.toString()
            return true
        }
        return false
    }

    private fun registerJsonRequest() : RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("name", reg_studentname1)
            json.put("stu_class", spinnerView2_val1)
            json.put("phnum", reg_mobileno1)
            json.put("email", reg_email1)
            json.put("password", reg_pwd1)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun processRegister(){
        lifecycleScope.launch {
            showProgress(this@LoginActivity, "Processing..")
            val response =  APIManager.call<TuitionApiService, Response<RegisterResp>> {
                signupUser(registerJsonRequest())
            }
            try {
                if (response != null && response.body()!!.error==false){
                    dismissProgress()
                    //home page
                    startActivity(Intent(applicationContext, Home::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    //add to pref
                    saveUserInfo(response.body()!!.classid,response.body()!!.data)
                    finish()
                    Toast.makeText(this@LoginActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()

                }else{
                    dismissProgress()
                    Toast.makeText(this@LoginActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception) {
                dismissProgress()
                e.printStackTrace()

            }
        }
    }

    //get classes
    private fun getClasses(){
        lifecycleScope.launch {
            showProgress(this@LoginActivity, "Processing..")
            val response =  APIManager.call<TuitionApiService, Response<ClassResp>> {
                classes()
            }
            try {
                if (response != null && response.body()!!.error==false){
                    dismissProgress()
                    classes_data=response.body()!!.data
                    //setDatatoSpinner(classes_data)
                    //add data to spinner
                    setDatatoSpinner(classes_data)

                }else{
                    dismissProgress()


                }
            } catch (e: Exception) {
                dismissProgress()
                e.printStackTrace()

            }
        }
    }

    fun setDatatoSpinner(classes_data: List<ClassItem?>?)
    {
        classnames!!.clear()
        classnames!!.add(0,"Select Class")
        for (i in classes_data!!.indices) {
            classnames!!.add(classes_data[i]!!.className!!)
        }

        val spinnerArrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classnames!!)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view

        spinnerView2.adapter = spinnerArrayAdapter


        spinnerView2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {

                if(position>0)
                {
                    val classItem: ClassItem = classes_data[position-1]!!
                    spinnerView2_val1= classItem.id.toString()
                    //Toast.makeText(this@LoginActivity,"id"+spinnerView2_val1,Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // can leave this empty
            }
        }



    }

    fun forgotpassdialog()
    {
        val dialog = Dialog(this@LoginActivity)
        dialog.setContentView(R.layout.forgotpassword)

        dialog.setTitle("ALERT!!")

        val okbtn: Button = dialog.findViewById(R.id.okbtn) as Button
        val cancelbtn: Button = dialog.findViewById(R.id.cancelbtn) as Button

        val emailedt: EditText = dialog.findViewById(R.id.emailedittext) as EditText
        dialog.show()
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )

        cancelbtn.setOnClickListener(View.OnClickListener { // Close dialog
            dialog.dismiss()
        })
        okbtn.setOnClickListener(View.OnClickListener {
            if (emailedt.text.toString().isEmpty()){
                Toast.makeText(applicationContext, "Please enter emailId" , Toast.LENGTH_LONG).show()
            }else if(!isValidEmail(emailedt.text.toString())){
                Toast.makeText(applicationContext, "Please enter valid emailId" , Toast.LENGTH_LONG).show()
            }
            else
            {
                processForgotPassword(emailedt.text.toString(),dialog)
            }

        })
    }


    private fun processForgotPassword(email: String?, dialog: Dialog){
        lifecycleScope.launch {
            showProgress(this@LoginActivity, "Processing..")
            val response =  APIManager.call<TuitionApiService, Response<ForgotpassResp>> {
                forgotPassword(forgotJsonRequest(email))
            }
            try {
                if (response != null && response.body()!!.error==false){
                    dismissProgress()
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@LoginActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    dismissProgress()


                }
            } catch (e: Exception) {
                dismissProgress()
                e.printStackTrace()

            }
        }
    }

    private fun forgotJsonRequest(emailid:String?) : RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("registered_email", emailid)

            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}