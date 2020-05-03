package com.threedee.cache.db

/**
 * Defines constants for the db
 */
object DbConstants {
    //Farmers table
    const val FARMER_TABLE_NAME = "farmers"
    const val QUERY_FARMERS = "SELECT * FROM $FARMER_TABLE_NAME"

    //Farm Location table
    const val FARM_LOCATION_TABLE_NAME = "farm_locations"
}