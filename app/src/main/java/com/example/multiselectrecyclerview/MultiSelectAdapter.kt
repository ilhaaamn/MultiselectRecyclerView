package com.example.multiselectrecyclerview

import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.multiselectrecyclerview.databinding.HolderItemBinding

class MultiSelectAdapter(private val interaction: Interaction? = null, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<MultiSelectAdapter.MultiSelectViewHolder>(),
    ActionMode.Callback {

    private var multiSelect = false
    // Keeps track of all the selected images
    private val selectedItems = mutableListOf<Item>()
    private lateinit var actionMode: ActionMode

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectViewHolder {

        return MultiSelectViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.holder_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MultiSelectViewHolder, position: Int) {
        val currentItem = differ.currentList.get(position)
        val binding = HolderItemBinding.bind(holder.itemView)

        if (selectedItems.contains(currentItem)){
            binding.checked.visibility = View.VISIBLE
            binding.rlCardview.setBackgroundResource(R.drawable.background_category_cardview_selected)
        } else {
            binding.checked.visibility = View.GONE
            binding.rlCardview.setBackgroundResource(R.drawable.background_category_cardview)
        }

        binding.root.setOnLongClickListener {
            if (!multiSelect) {
                multiSelect = true
                activity.startSupportActionMode(this@MultiSelectAdapter)
                selectItem(binding, currentItem)
            }
            true
        }

        binding.root.setOnClickListener {
            if (multiSelect){
                selectItem(binding, currentItem)
            }
        }

        binding.title.text = currentItem.name
        binding.desc.text = currentItem.desc
    }

    // helper function that adds/removes an item to the list depending on the app's state
    private fun selectItem(binding: HolderItemBinding, item: Item) {
        // If the "selectedItems" list contains the item, remove it and set it's state to normal
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
            actionMode.title = selectedItems.count().toString() + " Selected"
            binding.checked.visibility = View.GONE
            binding.rlCardview.setBackgroundResource(R.drawable.background_category_cardview)

        } else {
            // Else, add it to the list and add a darker shade over the image, letting the user know that it was selected
            selectedItems.add(item)
            binding.checked.visibility = View.VISIBLE
            binding.rlCardview.setBackgroundResource(R.drawable.background_category_cardview_selected)
            actionMode.title = selectedItems.count().toString() + " Selected"
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Item>) {
        differ.submitList(list)
    }



    override fun onCreateActionMode(
        mode: ActionMode?,
        menu: Menu?
    ): Boolean {
        actionMode = mode!!
        val inflater: MenuInflater = actionMode.menuInflater
        inflater.inflate(R.menu.multiselect_menu, menu)
        return true
    }

    override fun onPrepareActionMode(
        mode: ActionMode?,
        menu: Menu?
    ): Boolean {
        actionMode.title = selectedItems.count().toString() + " Selected"
        return false
    }

    override fun onActionItemClicked(
        mode: ActionMode?,
        item: MenuItem?
    ): Boolean {
        println("Item Clicked")
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelect = false
        selectedItems.clear()
        notifyDataSetChanged()
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Item)
    }

    class MultiSelectViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

    }
}