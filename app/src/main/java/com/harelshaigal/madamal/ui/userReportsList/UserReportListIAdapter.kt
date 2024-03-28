package com.harelshaigal.madamal.ui.userReportsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportListItemBinding

class UserReportListIAdapter(private val fragmentManager: FragmentManager) : ListAdapter<Report, UserReportViewHolder>(UserReportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReportViewHolder {
        val binding = FragmentReportListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return UserReportViewHolder(
            parent.context, binding, fragmentManager
        )

    }

    override fun onBindViewHolder(holder: UserReportViewHolder, position: Int) {
        val currentReport: Report = getItem(position)
        holder.bind(currentReport)

    }
}

