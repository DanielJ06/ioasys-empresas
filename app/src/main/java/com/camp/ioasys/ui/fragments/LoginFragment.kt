package com.camp.ioasys.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.camp.ioasys.databinding.FragmentLoginBinding
import com.camp.ioasys.util.NetworkResult
import com.camp.ioasys.viewmodels.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel

    private var aT: String? = null
    private var c: String? = null
    private var u: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginSubmitButton.setOnClickListener {
            binding.loadingProgressBar.visibility = View.VISIBLE
            binding.whiteLoadingEffect.visibility = View.VISIBLE
            val email = binding.loginEmailEditText.text?.toString()
            val password = binding.loginPasswordEditText.text?.toString()

            if (email.isNullOrEmpty() && password.isNullOrEmpty()) {
                Snackbar.make(it, "Preencha os campos", Snackbar.LENGTH_SHORT).show()
            } else {
                onSubmit(email!!, password!!)
            }
        }

        authViewModel.userHeaders.observe(viewLifecycleOwner, { res ->
            when (res) {
                is NetworkResult.Success -> {
                    Log.i("FragmentsDebug", "loginSuccess")
                    binding.loadingProgressBar.visibility = View.INVISIBLE
                    binding.whiteLoadingEffect.visibility = View.INVISIBLE
                    val accessToken = res.data!!.get("access-token")
                    val client = res.data.get("client")
                    val uid = res.data.get("uid")

                    val imm: InputMethodManager =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                    imm.hideSoftInputFromWindow(view?.windowToken, 0)

                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                            accessToken,
                            client,
                            uid
                        )
                    )
                    authViewModel.userHeaders.postValue(null)
                }
                is NetworkResult.Loading -> { }
                is NetworkResult.Error -> {
                    Log.i("FragmentsDebug", "loginError")
                    binding.loadingProgressBar.visibility = View.INVISIBLE
                    binding.whiteLoadingEffect.visibility = View.INVISIBLE

                    binding.loginEmailInputLayout.error = " "
                    binding.loginPasswordInputLayout.error = " "

                    binding.loginErrorText.visibility = View.VISIBLE
                    binding.loginErrorText.text = res.message
                }
            }
        })

        return binding.root
    }

    private fun onSubmit(email: String, password: String) {
        authViewModel.signIn(email, password)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}