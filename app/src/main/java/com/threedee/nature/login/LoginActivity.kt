package com.threedee.nature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.threedee.domain.interactor.farm.LoginUser
import com.threedee.nature.R
import com.threedee.nature.databinding.ActivityLoginBinding
import com.threedee.nature.home.MainActivity
import com.threedee.nature.util.showSnackbar
import com.threedee.presentation.state.Resource
import com.threedee.presentation.state.ResourceState
import com.threedee.presentation.viewmodel.FarmViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private  lateinit var farmViewModel: FarmViewModel

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        initViewModel()
        initViews()
    }

    private fun initViews() {
        binding.loginButton.setOnClickListener {
            farmViewModel.loginUser(LoginUser.Params(binding.emailTextField.editText?.text.toString(),
                binding.passwordTextField.editText?.text.toString()))
        }
    }

    private fun initViewModel() {
        farmViewModel = ViewModelProvider(this, viewModelFactory).get(FarmViewModel::class.java)

        farmViewModel.loginUserLiveData.observe(this, Observer {resource ->
            handleLoginUser(resource)
        })
    }

    private fun handleLoginUser(resource: Resource<Unit>) {
        when(resource.status){
            ResourceState.LOADING -> {}
            ResourceState.SUCCESS -> {
                MainActivity.startActivity(this)
                finish()
            }
            ResourceState.ERROR -> {
                resource.message?.let { showSnackbar(it) }
            }
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}
