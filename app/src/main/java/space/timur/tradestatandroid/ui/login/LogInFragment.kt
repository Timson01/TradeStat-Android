package space.timur.tradestatandroid.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import space.timur.tradestatandroid.R
import space.timur.tradestatandroid.databinding.FragmentDescriptionBinding
import space.timur.tradestatandroid.databinding.FragmentLogInBinding
import space.timur.tradestatandroid.ui.welcome.WelcomeViewModel

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginBtn.setOnClickListener{
                viewModel.onButtonLogInClick()
            }
            signUpButton.setOnClickListener {
                viewModel.onButtonSignUpClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.logInEvent.collect{ event ->
                when(event){
                    is LoginViewModel.LogInEvent.NavigateToDealsScreen -> {
                        val action = LogInFragmentDirections.actionLogInFragmentToDealsFragment()
                        findNavController().navigate(action)
                    }
                    is LoginViewModel.LogInEvent.NavigateToRegisterScreen -> {
                        val action = LogInFragmentDirections.actionLogInFragmentToRegisterFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

}