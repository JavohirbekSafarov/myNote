package com.javohirbekcoder.mynote.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.javohirbekcoder.mynote.R
import com.javohirbekcoder.mynote.adapters.MainNotesAdapter
import com.javohirbekcoder.mynote.databinding.FragmentHomeBinding
import com.javohirbekcoder.mynote.models.MainRecyclerModel


class HomeFragment : Fragment(R.layout.fragment_home), MainNotesAdapter.myItemOnClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainNotesAdapter: MainNotesAdapter

    private var searching = false

    private val sortingNames = arrayListOf(
        "date modified",
        "olds first",
        "color",
        "title"
    )
    private var mainRecyclerItem = ArrayList<MainRecyclerModel>()
    private var searchingList = ArrayList<MainRecyclerModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        setStatusBarColor()
        setSpinner()
        setRecyclerAdapter()

        binding.searchImg.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (!searching) {
                    binding.searchImg.setImageResource(R.drawable.close)
                    binding.searchText.isEnabled = true
                    searching = true
                } else {
                    binding.searchImg.setImageResource(R.drawable.search)
                    binding.searchText.isEnabled = false
                    searching = false
                }
            }
            false
        }
        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createNoteFragment)
        }
        binding.searchText.doOnTextChanged { text, start, before, count ->
            search(text)
        }

        return binding.root
    }

    private fun search(text: CharSequence?) {
        searchingList.clear()
        for (list in mainRecyclerItem) {
            if (list.title.contains(text.toString(), true) || list.note.contains(
                    text.toString(),
                    true
                )
            ) {
                searchingList.add(list)
            }
        }

        mainNotesAdapter = MainNotesAdapter(requireContext(), searchingList, this)
        binding.mainRecyclerview.adapter = mainNotesAdapter
        binding.allNotesCount.text = mainNotesAdapter.itemCount.toString()
    }

    private fun setRecyclerAdapter() {
        mainRecyclerItem.add(MainRecyclerModel(2, "3", "note", "11.02.2022"))
        mainRecyclerItem.add(MainRecyclerModel(1, "1", "note", "12.02.2022"))
        mainRecyclerItem.add(MainRecyclerModel(3, "2", "note", "09.02.2022"))
        mainRecyclerItem.add(MainRecyclerModel(0, "5", "note last", "08.02.2022"))
        mainRecyclerItem.add(MainRecyclerModel(3, "4", "note", "13.02.2022"))
        mainNotesAdapter = MainNotesAdapter(requireContext(), mainRecyclerItem, this)
        binding.mainRecyclerview.adapter = mainNotesAdapter
        binding.allNotesCount.text = mainNotesAdapter.itemCount.toString()
    }

    private fun setStatusBarColor() {
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#5678F1")
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.sort_spinner_item,
            R.id.spinner_tv,
            sortingNames
        )
        adapter.setDropDownViewResource(R.layout.sort_spinner_item)

        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    requireContext(),
                    parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_LONG
                ).show()
                 when (position) {
                     0 -> {
                         mainRecyclerItem.sortBy { it.time }
                         mainRecyclerItem.reverse()
                     }
                     1 -> {
                         mainRecyclerItem.sortBy { it.time }
                     }
                     2 -> {
                         mainRecyclerItem.sortBy { it.colorIndex }
                     }
                     3 -> {
                         mainRecyclerItem.sortBy { it.title }
                     }
                     else -> {
                         mainRecyclerItem.sortBy { it.time }
                     }
                 }
               /* mainRecyclerItem.sortBy {
                    when (position) {
                        0 -> {
                            it.time
                        }
                        1 -> {
                            it.time
                        }
                        //add color
                        2 -> {
                            it.time
                            mainRecyclerItem.reverse()
                        }
                        3 -> {
                            it.time
                        }
                        else -> {
                            it.time
                        }
                    }
                }*/
                //mainRecyclerItem.clear()
                //mainRecyclerItem.addAll(newList)
                mainNotesAdapter.notifyItemRangeChanged(0, mainRecyclerItem.size)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun mItemClickListener(position: Int) {
        Toast.makeText(requireContext(), mainRecyclerItem[position].title, Toast.LENGTH_SHORT).show()
        val bundle =  Bundle()
        bundle.putString("title", mainRecyclerItem[position].title)
        bundle.putString("note", mainRecyclerItem[position].note)
        bundle.putString("time", mainRecyclerItem[position].time)
        bundle.putInt("colorIndex", mainRecyclerItem[position].colorIndex)
        findNavController().navigate(R.id.action_homeFragment_to_openNoteFragment, bundle)
    }

    override fun onLongClickListener(position: Int) {
        Toast.makeText(requireContext(), "Deleting?", Toast.LENGTH_SHORT).show()
    }

}