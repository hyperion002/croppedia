package com.example.croppedia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.croppedia.databinding.FragmentDetailsBinding
import com.example.croppedia.utils.Constants.BASE_URL

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val crop = args.crop
        binding.imageviewCropImage.load("$BASE_URL${crop.image}")
        binding.textviewCropName.text = crop.name
        binding.textviewCropType.text = crop.type
        binding.textviewCropAbout.text = crop.about
        binding.textviewCropSeason.text = crop.season
        crop.climateRequirements.forEach {
            binding.textviewCropClimateRequirements.append("-> $it\n")
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}