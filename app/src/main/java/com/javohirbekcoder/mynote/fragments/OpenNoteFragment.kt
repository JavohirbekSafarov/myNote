package com.javohirbekcoder.mynote.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.javohirbekcoder.mynote.R
import com.javohirbekcoder.mynote.databinding.FragmentOpenNoteBinding

class OpenNoteFragment : Fragment(R.layout.fragment_open_note) {

    private var _binding: FragmentOpenNoteBinding? = null
    private val binding get() = _binding!!

    private val firstEdit = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenNoteBinding.inflate(inflater)

        val colorIndex = requireArguments().getInt("colorIndex")
        val title = requireArguments().getString("title")
        val note  = requireArguments().getString("note")
        val time  = requireArguments().getString("time")

        binding.titeText.setText(title)
        binding.titleTextMain.setText(title)
        binding.noteText.setText(note)
        binding.timeTV.setText(time)

        binding.saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
        }
        binding.selectColorBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
        }
        binding.deleteBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigate(R.id.action_openNoteFragment_to_homeFragment)
        }
    }
}