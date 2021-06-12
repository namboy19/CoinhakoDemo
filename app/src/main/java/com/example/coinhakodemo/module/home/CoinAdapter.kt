package com.example.coinhakodemo.module.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coinhakodemo.R
import com.example.coinhakodemo.databinding.ItemViewCoinBinding
import com.example.coinhakodemo.model.home.CoinResponse

class CoinAdapter : RecyclerView.Adapter<CoinHolder>() {

    private var items = listOf<CoinResponse>()

    fun setData(data: List<CoinResponse>) {
        items = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinHolder {
        return CoinHolder(
            ItemViewCoinBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CoinHolder, position: Int) {
        holder.binding.ivcTxtName.text = items[position].name + "(${items[position].base})"
        holder.binding.ivcTxtBuy.text = items[position].getBuyPrice()
        holder.binding.ivcTxtSell.text = items[position].getSellPrice()

    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class CoinHolder(val binding: ItemViewCoinBinding) : RecyclerView.ViewHolder(binding.root) {
}
