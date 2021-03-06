package com.camp.ioasys.bindingAdapters

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.camp.ioasys.R
import com.camp.ioasys.models.Company
import com.camp.ioasys.ui.fragments.HomeFragmentDirections


class CompaniesRowBindingAdapter {

    companion object {

        @BindingAdapter("onCompanyClickListener")
        @JvmStatic
        fun onCompanyClickListener(
            rowLayout: ConstraintLayout, company: Company
        ) {
            rowLayout.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    company.enterpriseName,
                    company.description,
                    company.city,
                    company.photo
                )
                rowLayout.findNavController().navigate(action)
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun onLoadImage(
            image: ImageView, imageUrl: String
        ) {
            image.load("https://empresas.ioasys.com.br/$imageUrl") {
                crossfade(200)
                error(R.color.darker_pink)
            }
        }

    }

}