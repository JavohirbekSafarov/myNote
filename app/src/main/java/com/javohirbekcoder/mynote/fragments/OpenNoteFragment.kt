package com.javohirbekcoder.mynote.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.javohirbekcoder.mynote.R
import com.javohirbekcoder.mynote.data.AppData
import com.javohirbekcoder.mynote.database.DatabaseHelper
import com.javohirbekcoder.mynote.databinding.FragmentOpenNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class OpenNoteFragment : Fragment(R.layout.fragment_open_note) {

    private var _binding: FragmentOpenNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper

    private var selectedColorIndex = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenNoteBinding.inflate(inflater)

        databaseHelper = DatabaseHelper(requireContext())

        val index = requireArguments().getInt("id")
        val colorIndex = requireArguments().getInt("colorIndex")
        var title = requireArguments().getString("title")
        var note  = requireArguments().getString("note")
        var time  = requireArguments().getString("time")

        binding.titeText.setText(title)
        binding.titleTextMain.setText(title)
        binding.noteText.setText(note)
        binding.timeTV.setText(time)
        setTheme(colorIndex)


        binding.saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Update", Toast.LENGTH_SHORT).show()

            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault())
            val formattedDateTime = dateFormat.format(currentDateTime)
            title = binding.titeText.text.toString()
            note = binding.noteText.text.toString()

            if (checkData()){
                databaseHelper.updateNote(index.toString(), title!!, note!!, formattedDateTime, selectedColorIndex)
            }
        }
        binding.selectColorBtn.setOnClickListener {
            val select_color_dialog = Dialog(requireContext())
            select_color_dialog.setContentView(R.layout.color_picker_dialog)
            select_color_dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            val greenBtn = select_color_dialog.findViewById<ImageView>(R.id.color_picker_green)
            val pinkBtn = select_color_dialog.findViewById<ImageView>(R.id.color_picker_pink)
            val yellowBtn = select_color_dialog.findViewById<ImageView>(R.id.color_picker_yellow)
            val blueBtn = select_color_dialog.findViewById<ImageView>(R.id.color_picker_blue)

            greenBtn.setOnClickListener {
                setTheme(0)
                selectedColorIndex = 0
                select_color_dialog.dismiss()
            }
            pinkBtn.setOnClickListener {
                setTheme(1)
                selectedColorIndex = 1
                select_color_dialog.dismiss()
            }
            yellowBtn.setOnClickListener {
                setTheme(2)
                selectedColorIndex = 2
                select_color_dialog.dismiss()
            }
            blueBtn.setOnClickListener {
                setTheme(3)
                selectedColorIndex = 3
                select_color_dialog.dismiss()
            }

            select_color_dialog.show()
        }
        binding.deleteBtn.setOnClickListener {
            if (databaseHelper.deleteNote(title!!)){
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.action_openNoteFragment_to_homeFragment)
        }

        binding.myScrollView.setOnClickListener {

            binding.titeText.visibility = View.VISIBLE
            binding.saveBtn.visibility = View.VISIBLE
            binding.selectColorBtn.visibility = View.VISIBLE
            binding.noteText.requestFocus()

        }

        binding.titeText.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty())
                binding.titleTextMain.text = "Your title"
            else
                binding.titleTextMain.text = text
        }

        binding.noteText.setOnClickListener {
            binding.titeText.visibility = View.VISIBLE
            binding.saveBtn.visibility = View.VISIBLE
            binding.selectColorBtn.visibility = View.VISIBLE
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        return binding.root
    }

    private fun checkData(): Boolean {
        return !(binding.noteText.text.isNullOrEmpty() || binding.titeText.text.isNullOrEmpty())
    }

    private fun setTheme(index: Int) {

        val statusColor = AppData.StatusBarColors[index]
        val actionColor = AppData.ActionBarColors[index]
        //Status Bar
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(statusColor)

        //Action Bar
        binding.linear.setBackgroundColor(Color.parseColor(actionColor))
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