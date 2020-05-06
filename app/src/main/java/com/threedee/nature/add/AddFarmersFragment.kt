package com.threedee.nature.add

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.threedee.domain.model.Farmer
import com.threedee.nature.R
import com.threedee.nature.databinding.LayoutFarmerDetailsBinding
import com.threedee.nature.util.isValidEmail
import com.threedee.nature.util.isValidName
import com.threedee.nature.util.isValidPhone
import com.threedee.nature.util.showSnackbar
import com.threedee.nature.util.validate
import com.threedee.presentation.viewmodel.FarmViewModel
import dagger.android.support.DaggerFragment
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class AddFarmersFragment : DaggerFragment() {
    private lateinit var binding: LayoutFarmerDetailsBinding
    var currentPhotoPath: String = ""
    val REQUEST_TAKE_PHOTO = 1
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private  lateinit var farmViewModel: FarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.layout_farmer_details, container, false
        )
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.takePhotoButton.setOnClickListener {
            dispatchTakePictureIntent(view.context)
        }
        binding.nextButton.setOnClickListener {
            if (validateUserInput()){
                // Go to next page
                farmViewModel.currentPage.value = 1
                val farmer = Farmer(-1, binding.nameTextField.editText?.text.toString(), binding.phoneTextField.editText?.text.toString(),
                    currentPhotoPath, binding.emailTextField.editText?.text.toString(), 0L)
                farmViewModel.farmer.value = farmer
            } else {
                activity?.showSnackbar("Fields marked * are compulsory")
            }
        }
    }

    private fun initViewModel() {
        activity?.let {
            farmViewModel = ViewModelProvider(it, viewModelFactory).get(FarmViewModel::class.java)
        }
    }

    private fun validateUserInput(): Boolean {
        binding.emailTextField.editText?.validate("Valid e-mail required!") {data -> data.isValidEmail()}
        binding.phoneTextField.editText?.validate("Valid phone number required!") {data -> data.isValidPhone()}
        binding.nameTextField.editText?.validate("Valid name required!") {data -> data.isValidName()}

        return binding.nameTextField.editText?.text.toString().isValidName() && binding.emailTextField.editText?.text.toString().isValidEmail() &&
            binding.phoneTextField.editText?.text.toString().isValidPhone() && currentPhotoPath.isNotEmpty()
    }

    private fun setProfileImage(url: String) {
        Timber.e("Photo url: $url")
        Glide.with(this)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .fallback(R.drawable.ic_person)
            .into(binding.profileImageView)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent(context: Context) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        "com.threedee.nature.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setProfileImage(currentPhotoPath)
        }
    }
}