package com.example.croppedia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.croppedia.databinding.CropItemBinding
import com.example.croppedia.model.Crop
import com.example.croppedia.ui.fragment.HomeFragmentDirections
import com.example.croppedia.utils.Constants.BASE_URL
import com.example.croppedia.utils.DiffUtilTemplate

class CropAdapter : RecyclerView.Adapter<CropAdapter.CropViewHolder>() {

    private var crops = emptyList<Crop>()
    class CropViewHolder(private val binding: CropItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crop: Crop) {
            binding.root.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(crop)
                it.findNavController().navigate(action)
            }
            binding.textviewName.text = crop.name
            binding.textviewType.text = crop.type
            binding.imageviewCrop.load("$BASE_URL${crop.image}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        return CropViewHolder(
            CropItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = crops.size

    override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
        val currentCrop = crops[position]
        holder.bind(currentCrop)
    }

    fun setData(cropsList: List<Crop>) {
        val diffUtilTemplate = DiffUtilTemplate(crops, cropsList)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilTemplate)
        crops = cropsList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}