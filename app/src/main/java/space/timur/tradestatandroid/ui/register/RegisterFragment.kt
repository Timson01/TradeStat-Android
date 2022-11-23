package space.timur.tradestatandroid.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import space.timur.tradestatandroid.R
import space.timur.tradestatandroid.databinding.FragmentRegisterBinding
import space.timur.tradestatandroid.ui.login.LogInFragmentDirections
import space.timur.tradestatandroid.ui.login.LoginViewModel

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnCreateAccount.setOnClickListener{
                viewModel.onButtonRegisterClick()
            }
            logInButton.setOnClickListener {
                viewModel.onButtonLogInClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.registerEvent.collect{ event ->
                when(event){
                    is RegisterViewModel.LogInEvent.NavigateToLogInScreen -> {
                        val action = RegisterFragmentDirections.actionRegisterFragmentToLogInFragment()
                        findNavController().navigate(action)
                    }
                    is RegisterViewModel.LogInEvent.NavigateToDealsScreen -> {
                        val action = RegisterFragmentDirections.actionRegisterFragmentToDealsFragment()
                        findNavController().navigate(action)
                    }
                }

            }
        }

    }
}