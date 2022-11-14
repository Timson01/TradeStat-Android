package space.timur.tradestatandroid.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import space.timur.tradestatandroid.databinding.FragmentWelcomeBinding
import space.timur.tradestatandroid.ui.description.DescriptionFragmentDirections
import space.timur.tradestatandroid.ui.description.DescriptionViewModel

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSignUp.setOnClickListener {
                viewModel.onButtonSignUpClick()
            }
            btnLogin.setOnClickListener {
                viewModel.onButtonLogInClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.welcomeEvent.collect { event ->
                when (event) {
                    is WelcomeViewModel.WelcomeEvent.NavigateToRegisterScreen -> {
                        val action =
                            WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment()
                        findNavController().navigate(action)
                    }
                    is WelcomeViewModel.WelcomeEvent.NavigateToLogInScreen -> {
                        val action =
                            WelcomeFragmentDirections.actionWelcomeFragmentToLogInFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }

    }
}