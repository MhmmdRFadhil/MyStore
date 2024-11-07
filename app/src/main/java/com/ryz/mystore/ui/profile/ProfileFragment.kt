package com.ryz.mystore.ui.profile

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ryz.mystore.databinding.FragmentProfileBinding
import com.ryz.mystore.utils.showMessage

class ProfileFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() = with(binding) {
        ivGithub.setOnClickListener { openUrl(GITHUB_LINK) }
        ivLinkedIn.setOnClickListener { openUrl(LINKED_IN_LINK) }
        ivInstagram.setOnClickListener { openUrl(INSTAGRAM_LINK) }
    }

    private fun openUrl(url: String) {
        val intent = Intent(ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (_: Exception) {
            showMessage("Something went wrong!")
        }
    }

    companion object {
        const val GITHUB_LINK = "https://github.com/MhmmdRFadhil"
        const val LINKED_IN_LINK = "https://www.linkedin.com/in/rzqnfadhil/"
        const val INSTAGRAM_LINK = "https://www.instagram.com/rzqnfdhl/"
    }
}