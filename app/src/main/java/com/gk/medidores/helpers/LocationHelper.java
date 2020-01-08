package com.gk.medidores.helpers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationHelper {
    public static void getLocation(final Context ctx, LocationListener listener) throws SecurityException {
        LocationManager svc = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        Location lastKnowLocation = svc.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(lastKnowLocation != null) {
            listener.onLocationChanged(lastKnowLocation);
        } else if(svc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            svc.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
        } else {
            listener.onLocationChanged(null);
        }
    }
}
