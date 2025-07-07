package com.maddoxh.content.energy

/**
 * Interface representing an object that can store energy in EU (Electronium Units)
 */
interface EnergyStorage {
    /**
     * Insert energy into the storage
     *
     * @param amount The amount of energy to insert
     * @param simulate If true, does not insert the energy
     * @return The amount of energy accepted
     */
    fun insert(amount: Long, simulate: Boolean): Long

    /**
     * Extract energy from the storage
     *
     * @param amount The amount of energy to extract
     * @param simulate If true, does not extract the energy
     * @return The amount of energy extracted
     */
    fun extract(amount: Long, simulate: Boolean): Long

    fun getStored(): Long
    fun getCapacity(): Long
}