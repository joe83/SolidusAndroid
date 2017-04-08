package com.scanba.solidusandroid.sqlite;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.scanba.solidusandroid.models.CartItem;
import com.scanba.solidusandroid.models.Order;
import com.scanba.solidusandroid.models.taxonomy.Taxon;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[]{Taxon.class, CartItem.class, Order.class};

    public static void main(String[] args) throws IOException, SQLException {

        writeConfigFile("ormlite_config.txt", classes);

    }
}
