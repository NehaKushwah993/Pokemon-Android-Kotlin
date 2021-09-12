package com.nehak.pokemonlist.ui.pokemonList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.databinding.AdapterRowPokemonBinding
import com.nehak.pokemonlist.utils.diffUtil.DiffCallBackPokemonModel
import com.nehak.pokemonlist.utils.interfaces.OnPokemonClickListener
import kotlin.collections.ArrayList

class PokemonAdapter() : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    val pokemonList: ArrayList<PokemonModel> = ArrayList()
    var onPokemonClickListener: OnPokemonClickListener? = null

    fun setPokemonList(newPokemonList: List<PokemonModel>?) {
        val diffCallBackPokemonModel =
            DiffCallBackPokemonModel(this.pokemonList, newPokemonList)
        val diffResult = DiffUtil.calculateDiff(diffCallBackPokemonModel)
        pokemonList.clear()
        if (newPokemonList != null) pokemonList.addAll(newPokemonList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun clear() {
        val diffCallBackPokemonModel =
            DiffCallBackPokemonModel(this.pokemonList, null)
        val diffResult = DiffUtil.calculateDiff(diffCallBackPokemonModel)
        pokemonList.clear()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding =
            AdapterRowPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false);
        return PokemonViewHolder(binding);
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.binding.apply {
            pokemon = getItem(position)
            executePendingBindings()
        }
        holder.binding.root.setOnClickListener {
            onPokemonClickListener?.onPokemonClick(position, pokemonList[position], holder.binding.ivPokemon)
        }
    }

    private fun getItem(position: Int): PokemonModel {
        return pokemonList[position]
    }

    class PokemonViewHolder(val binding: AdapterRowPokemonBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return pokemonList.size
    }


}
