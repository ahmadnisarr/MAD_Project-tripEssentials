import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tripadvisor.adventures
import com.example.tripadvisor.countries
import com.example.tripadvisor.hotel
import com.example.tripadvisor.hotelDetails
import com.example.tripadvisor.resturant
import com.example.tripadvisor.resturantDetails
import com.example.tripadvisor.userAdventures

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app_database.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_ADVENTURES = "adventures"
        private const val TABLE_COUNTRIES = "countries"
        private const val TABLE_HOTEL = "hotel"
        private const val TABLE_HOTEL_DETAILS = "hotelDetails"
        private const val TABLE_RESTURANT = "resturant"
        private const val TABLE_RESTURANT_DETAILS = "resturantDetails"
        private const val TABLE_USER_ADVENTURES = "userAdventures"

        private const val KEY_ID = "Id"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createUserAdventuresTable = ("CREATE TABLE $TABLE_USER_ADVENTURES ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "userID TEXT,"
                + "adventureID INTEGER)")

        val createAdventuresTable = ("CREATE TABLE $TABLE_ADVENTURES ("
                + "$KEY_ID INTEGER PRIMARY KEY, "
                + "name TEXT, "
                + "price REAL, "
                + "image TEXT, "
                + "about TEXT, "
                + "hightlights TEXT)")

        val createCountriesTable = ("CREATE TABLE $TABLE_COUNTRIES ("
                + "$KEY_ID INTEGER PRIMARY KEY, "
                + "name TEXT, "
                + "imagePath TEXT)")

        val createHotelTable = ("CREATE TABLE $TABLE_HOTEL ("
                + "$KEY_ID INTEGER PRIMARY KEY, "
                + "countryID INTEGER, "
                + "name TEXT, "
                + "price REAL, "
                + "officalSite TEXT, "
                + "images TEXT)")

        val createHotelDetailsTable = ("CREATE TABLE $TABLE_HOTEL_DETAILS ("
                + "$KEY_ID INTEGER PRIMARY KEY, "
                + "hotelID INTEGER, "
                + "phoneNumber TEXT, "
                + "email TEXT, "
                + "websiteLink TEXT, "
                + "about TEXT, "
                + "address TEXT)")

        val createResturantTable = ("CREATE TABLE $TABLE_RESTURANT ("
                + "$KEY_ID INTEGER PRIMARY KEY , "
                + "countryId INTEGER, "
                + "name TEXT, "
                + "menu TEXT, "
                + "images TEXT)")

        val createResturantDetailsTable = ("CREATE TABLE $TABLE_RESTURANT_DETAILS ("
                + "$KEY_ID INTEGER PRIMARY KEY, "
                + "resturantId INTEGER, "
                + "websitelink TEXT, "
                + "phoneNumber TEXT, "
                + "address TEXT, "
                + "about TEXT, "
                + "time TEXT)")

        db.execSQL(createAdventuresTable)
        db.execSQL(createCountriesTable)
        db.execSQL(createHotelTable)
        db.execSQL(createHotelDetailsTable)
        db.execSQL(createResturantTable)
        db.execSQL(createResturantDetailsTable)
        db.execSQL(createUserAdventuresTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADVENTURES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COUNTRIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HOTEL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HOTEL_DETAILS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESTURANT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESTURANT_DETAILS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_ADVENTURES")
        onCreate(db)
    }

    // Helper methods to insert data into tables
    private fun insert(tableName: String, values: ContentValues): Long {
        val db = writableDatabase
        return db.insert(tableName, null, values)
    }

    private fun update(tableName: String, values: ContentValues, id: Int): Int {
        val db = writableDatabase
        return db.update(tableName, values, "$KEY_ID = ?", arrayOf(id.toString()))
    }

    private fun delete(tableName: String, id: Int): Int {
        val db = writableDatabase
        return db.delete(tableName, "$KEY_ID = ?", arrayOf(id.toString()))
    }

    private fun query(tableName: String, selection: String?, selectionArgs: Array<String>?): Cursor {
        val db = readableDatabase
        return db.query(tableName, null, selection, selectionArgs, null, null, null)
    }

    // Function implementations
    fun getAllCountry(): List<countries> {
        val countriesList = mutableListOf<countries>()
        val cursor = query(TABLE_COUNTRIES, null, null)
        if (cursor.moveToFirst()) {
            do {
                val country = countries().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"))
                }
                countriesList.add(country)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return countriesList
    }

    fun getAllAdventure(): List<adventures> {
        val adventuresList = mutableListOf<adventures>()
        val cursor = query(TABLE_ADVENTURES, null, null)
        if (cursor.moveToFirst()) {
            do {
                val adventure = adventures().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                    image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                    about = cursor.getString(cursor.getColumnIndexOrThrow("about"))
                    hightlights = cursor.getString(cursor.getColumnIndexOrThrow("hightlights"))
                }
                adventuresList.add(adventure)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return adventuresList
    }

    fun getAllResturant(): List<resturant> {
        val resturantsList = mutableListOf<resturant>()
        val cursor = query(TABLE_RESTURANT, null, null)
        if (cursor.moveToFirst()) {
            do {
                val resturant = resturant().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                    countryId = cursor.getInt(cursor.getColumnIndexOrThrow("countryId"))
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    menu = cursor.getString(cursor.getColumnIndexOrThrow("menu"))
                    images = cursor.getString(cursor.getColumnIndexOrThrow("images"))
                }
                resturantsList.add(resturant)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return resturantsList
    }

    fun getResturantByCountryId(CountryId: Int): List<resturant> {
        val resturantsList = mutableListOf<resturant>()
        val cursor = query(TABLE_RESTURANT, "countryId = ?", arrayOf(CountryId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val resturant = resturant().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                    countryId = cursor.getInt(cursor.getColumnIndexOrThrow("countryId"))
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    menu = cursor.getString(cursor.getColumnIndexOrThrow("menu"))
                    images = cursor.getString(cursor.getColumnIndexOrThrow("images"))
                }
                resturantsList.add(resturant)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return resturantsList
    }

    fun getResturantDetailByResturantId(ResturantId: Int): resturantDetails {
        val cursor = query(TABLE_RESTURANT_DETAILS, "resturantId = ?", arrayOf(ResturantId.toString()))
        return if (cursor.moveToFirst()) {
            resturantDetails().apply {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                resturantId = cursor.getInt(cursor.getColumnIndexOrThrow("resturantId"))
                websitelink = cursor.getString(cursor.getColumnIndexOrThrow("websitelink"))
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
                address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                about = cursor.getString(cursor.getColumnIndexOrThrow("about"))
                time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
            }
        } else {
            resturantDetails().apply {
                // Initializ
                // e with default values
                id = 0
                resturantId = 0
                websitelink = ""
                phoneNumber = ""
                about = ""
                address = ""
                time= ""
            }
        }.also {
            cursor.close()
        }
    }

    fun getResturantByID(Id: Int): resturant {
        val cursor = query(TABLE_RESTURANT, "$KEY_ID = ?", arrayOf(Id.toString()))
        return if (cursor.moveToFirst()) {
            resturant().apply {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                countryId = cursor.getInt(cursor.getColumnIndexOrThrow("countryId"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                menu = cursor.getString(cursor.getColumnIndexOrThrow("menu"))
                images = cursor.getString(cursor.getColumnIndexOrThrow("images"))
            }
        } else {
            resturant().apply {
                // Initialize with default values
                id = 0
                name = ""
                countryId= 0
                menu = ""
                images = ""
            }
        }.also {
            cursor.close()
        }
    }

    fun getAdventureById(Id: Int): adventures {
        val cursor = query(TABLE_ADVENTURES, "$KEY_ID = ?", arrayOf(Id.toString()))
        return if (cursor.moveToFirst()) {
            adventures().apply {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                about = cursor.getString(cursor.getColumnIndexOrThrow("about"))
                hightlights = cursor.getString(cursor.getColumnIndexOrThrow("hightlights"))
            }
        } else {
            adventures().apply {
                // Initialize with default values
                id = 0
                name = ""
                price = 0.0
                image = ""
                about = ""
                hightlights = ""
            }
        }.also {
            cursor.close()
        }
    }

    fun getHotelById(Id: Int): hotel {
        val cursor = query(TABLE_HOTEL, "$KEY_ID = ?", arrayOf(Id.toString()))
        return if (cursor.moveToFirst()) {
            hotel().apply {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                countryID = cursor.getInt(cursor.getColumnIndexOrThrow("countryID"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                officalSite = cursor.getString(cursor.getColumnIndexOrThrow("officalSite"))
                images = cursor.getString(cursor.getColumnIndexOrThrow("images"))
            }
        } else {
            hotel().apply {
                // Initialize with default values
                id = 0
                name = ""
                price = 0.0
                countryID = 0
                officalSite = ""
                images = ""
            }
        }.also {
            cursor.close()
        }
    }

    fun getHotelByCountryId(countryId: Int): List<hotel> {
        val hotelsList = mutableListOf<hotel>()
        val cursor = query(TABLE_HOTEL, "countryID = ?", arrayOf(countryId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val hotel = hotel().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                    countryID = cursor.getInt(cursor.getColumnIndexOrThrow("countryID"))
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                    officalSite = cursor.getString(cursor.getColumnIndexOrThrow("officalSite"))
                    images = cursor.getString(cursor.getColumnIndexOrThrow("images"))
                }
                hotelsList.add(hotel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return hotelsList
    }

    fun getHotelDetailsByHotelId(hotelId: Int): hotelDetails {
        val cursor = query(TABLE_HOTEL_DETAILS, "hotelID = ?", arrayOf(hotelId.toString()))
        return if (cursor.moveToFirst()) {
            hotelDetails().apply {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                hotelID = cursor.getInt(cursor.getColumnIndexOrThrow("hotelID"))
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
                email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                websiteLink = cursor.getString(cursor.getColumnIndexOrThrow("websiteLink"))
                about = cursor.getString(cursor.getColumnIndexOrThrow("about"))
                address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
            }
        } else {
            hotelDetails().apply {
                // Initialize with default values
                id = 0
                hotelID = 0
                phoneNumber = ""
                email = ""
                about = ""
                websiteLink = ""
                address=""
            }
        }.also {
            cursor.close()
        }
    }


    // Add or replace a single country in the database
    fun addCountry(country: countries) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Id", country.id)
            put("name", country.name)
            put("imagePath", country.imagePath)
        }
        db.replace(TABLE_COUNTRIES, null, values)
    }

    // Add or replace a single adventure in the database
    fun addAdventure(adventure: adventures) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Id", adventure.id)
            put("name", adventure.name)
            put("price", adventure.price)
            put("image", adventure.image)
            put("about", adventure.about)
            put("hightlights", adventure.hightlights)
        }
        db.replace(TABLE_ADVENTURES, null, values)
    }

    // Add or replace a single restaurant in the database
    fun addResturant(resturant: resturant) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Id", resturant.id)
            put("countryId", resturant.countryId)
            put("name", resturant.name)
            put("menu", resturant.menu)
            put("images", resturant.images)
        }
        db.replace(TABLE_RESTURANT, null, values)
    }

    // Add or replace a single hotel in the database
    fun addHotel(hotel: hotel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Id", hotel.id)
            put("countryID", hotel.countryID)
            put("name", hotel.name)
            put("price", hotel.price)
            put("officalSite", hotel.officalSite)
            put("images", hotel.images)
        }
        db.replace(TABLE_HOTEL, null, values)
    }

    // Add or replace a single hotel detail in the database
    fun addHotelDetail(hotelDetail: hotelDetails) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Id", hotelDetail.id)
            put("hotelID", hotelDetail.hotelID)
            put("phoneNumber", hotelDetail.phoneNumber)
            put("email", hotelDetail.email)
            put("websiteLink", hotelDetail.websiteLink)
            put("about", hotelDetail.about)
            put("address", hotelDetail.address)
        }
        db.replace(TABLE_HOTEL_DETAILS, null, values)
    }

    // Add or replace a single restaurant detail in the database
    fun addResturantDetail(resturantDetail: resturantDetails) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Id", resturantDetail.id)
            put("resturantId", resturantDetail.resturantId)
            put("websitelink", resturantDetail.websitelink)
            put("phoneNumber", resturantDetail.phoneNumber)
            put("address", resturantDetail.address)
            put("about", resturantDetail.about)
            put("time", resturantDetail.time)
        }
        db.replace(TABLE_RESTURANT_DETAILS, null, values)
    }


    // Add or replace a list of countries in the database
    fun addCountries(countriesList: List<countries>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (country in countriesList) {
                val values = ContentValues().apply {
                    put("Id", country.id)
                    put("name", country.name)
                    put("imagePath", country.imagePath)
                }
                db.replace(TABLE_COUNTRIES, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Add or replace a list of adventures in the database
    fun addAdventures(adventuresList: List<adventures>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (adventure in adventuresList) {
                val values = ContentValues().apply {
                    put("Id", adventure.id)
                    put("name", adventure.name)
                    put("price", adventure.price)
                    put("image", adventure.image)
                    put("about", adventure.about)
                    put("hightlights", adventure.hightlights)
                }
                db.replace(TABLE_ADVENTURES, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Add or replace a list of restaurants in the database
    fun addResturants(resturantsList: List<resturant>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (resturant in resturantsList) {
                val values = ContentValues().apply {
                    put("Id", resturant.id)
                    put("countryId", resturant.countryId)
                    put("name", resturant.name)
                    put("menu", resturant.menu)
                    put("images", resturant.images)
                }
                db.replace(TABLE_RESTURANT, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Add or replace a list of hotels in the database
    fun addHotels(hotelsList: List<hotel>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (hotel in hotelsList) {
                val values = ContentValues().apply {
                    put("Id", hotel.id)
                    put("countryID", hotel.countryID)
                    put("name", hotel.name)
                    put("price", hotel.price)
                    put("officalSite", hotel.officalSite)
                    put("images", hotel.images)
                }
                db.replace(TABLE_HOTEL, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Add or replace a list of hotel details in the database
    fun addHotelDetails(hotelDetailsList: List<hotelDetails>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (hotelDetail in hotelDetailsList) {
                val values = ContentValues().apply {
                    put("Id", hotelDetail.id)
                    put("hotelID", hotelDetail.hotelID)
                    put("phoneNumber", hotelDetail.phoneNumber)
                    put("email", hotelDetail.email)
                    put("websiteLink", hotelDetail.websiteLink)
                    put("about", hotelDetail.about)
                    put("address", hotelDetail.address)
                }
                db.replace(TABLE_HOTEL_DETAILS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Add or replace a list of restaurant details in the database
    fun addResturantDetails(resturantDetailsList: List<resturantDetails>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (resturantDetail in resturantDetailsList) {
                val values = ContentValues().apply {
                    put("Id", resturantDetail.id)
                    put("resturantId", resturantDetail.resturantId)
                    put("websitelink", resturantDetail.websitelink)
                    put("phoneNumber", resturantDetail.phoneNumber)
                    put("address", resturantDetail.address)
                    put("about", resturantDetail.about)
                    put("time", resturantDetail.time)
                }
                db.replace(TABLE_RESTURANT_DETAILS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


    fun addUserAdventure(userAdventure: userAdventures): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("userID", userAdventure.userID)
            put("adventureID", userAdventure.adventureID)
        }

        // Inserting the row
        val success = db.insert(TABLE_USER_ADVENTURES, null, contentValues)
        db.close()
        return success
    }


    fun getUserAdventures(userID: String?): List<userAdventures> {
        val userAdventuresList = mutableListOf<userAdventures>()
        val selectQuery = "SELECT * FROM $TABLE_USER_ADVENTURES WHERE userID = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(userID))

        if (cursor.moveToFirst()) {
            do {
                val userAdventure = userAdventures(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    userID = cursor.getString(cursor.getColumnIndexOrThrow("userID")),
                    adventureID = cursor.getInt(cursor.getColumnIndexOrThrow("adventureID"))
                )
                userAdventuresList.add(userAdventure)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userAdventuresList
    }

    fun getUserAdventuresListById(userAdventuresList: List<userAdventures>): List<adventures> {
        val adventuresList = mutableListOf<adventures>()
        val db = this.readableDatabase

        // Build a query string to select all adventures where adventureID matches
        val adventureIds = userAdventuresList.map { it.adventureID }.joinToString(", ")
        val selectQuery = "SELECT * FROM $TABLE_ADVENTURES WHERE $KEY_ID IN ($adventureIds)"

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val adventure = adventures(
                            id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                            name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                            image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                            about = cursor.getString(cursor.getColumnIndexOrThrow("about")),
                            hightlights = cursor.getString(cursor.getColumnIndexOrThrow("hightlights")),
                    // Add other fields from the adventure table here
                )
                adventuresList.add(adventure)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return adventuresList
    }

    fun deleteUserAdventure(adventureID: Int): Int {
        val db = this.writableDatabase
        // Deleting the row where adventureID matches
        val success = db.delete(TABLE_USER_ADVENTURES, "adventureID = ?", arrayOf(adventureID.toString()))
        db.close()
        return success
    }
    fun deleteAdventure(id: Int): Int {
        val db = this.writableDatabase
        // Deleting the row where ID matches
        val success = db.delete(TABLE_ADVENTURES, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return success
    }


    fun searchHotel(searchItem: String): List<hotel> {
        val hotelsList = mutableListOf<hotel>()
        val db = this.readableDatabase
        // Query with LIKE operator for partial matching
        val cursor = db.rawQuery("SELECT * FROM $TABLE_HOTEL WHERE name LIKE ?", arrayOf("%$searchItem%"))

        if (cursor.moveToFirst()) {
            do {
                val hotel = hotel().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                    countryID = cursor.getInt(cursor.getColumnIndexOrThrow("countryID"))
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                    officalSite = cursor.getString(cursor.getColumnIndexOrThrow("officalSite"))
                    images = cursor.getString(cursor.getColumnIndexOrThrow("images"))
                }
                hotelsList.add(hotel)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return hotelsList
    }
    fun searchResturant(searchItem: String): List<resturant> {
        val resturantsList = mutableListOf<resturant>()
        val db = this.readableDatabase
        // Query with LIKE operator for partial matching
        val cursor = db.rawQuery("SELECT * FROM $TABLE_RESTURANT WHERE name LIKE ?", arrayOf("%$searchItem%"))

        if (cursor.moveToFirst()) {
            do {
                val resturant = resturant().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                    countryId = cursor.getInt(cursor.getColumnIndexOrThrow("countryId"))
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    menu = cursor.getString(cursor.getColumnIndexOrThrow("menu"))
                    images = cursor.getString(cursor.getColumnIndexOrThrow("images"))
                }
                resturantsList.add(resturant)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return resturantsList
    }

}