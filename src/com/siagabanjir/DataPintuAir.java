package com.siagabanjir;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class DataPintuAir implements Parcelable, Comparable<DataPintuAir> {
	private String nama;
	private int[] tinggiAir;
	private int[] waktu;
	private String[] status;
	private int jumlahStatus;
	private String tanggal;
	private String tanggalShort;
	private String hari;
	private LatLng location;
	
	public static HashMap<String, LatLng> locationPintuAir;
	public static HashMap<String, DataPintuAir> mapsPintuAir;
	
	private final String[] hariArr = new String[]{"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
	private final static HashMap<String, Integer> statusCode = new HashMap<String, Integer>();
	static {

		statusCode.put("NORMAL", 4);
		statusCode.put("WASPADA", 3);
		statusCode.put("RAWAN", 2);
		statusCode.put("KRITIS", 1);
		locationPintuAir = new HashMap<String, LatLng>();
		locationPintuAir.put("Katulampa", new LatLng(-6.634091, 106.83718));
		locationPintuAir.put("Pesanggrahan", new LatLng(-6.396231, 106.772043));
		locationPintuAir.put("Depok", new LatLng(-6.400386, 106.831627));
		
		mapsPintuAir = new HashMap<String, DataPintuAir>();
	}
	public DataPintuAir(String nama) {
		super();
		this.nama = nama;
		this.waktu = new int[7];
		this.tinggiAir = new int[7];
		this.status = new String[7];
		for (int i=0; i<status.length; i++) {
			status[i] = "";
		}
		jumlahStatus = 0;
		tanggal = "";
		tanggalShort = "";
		hari = "";
		
		LatLng temp = locationPintuAir.get(nama);
		location = temp;
	}
	
	public DataPintuAir(Parcel p) {
		this.waktu = new int[7];
		this.tinggiAir = new int[7];
		this.status = new String[7];
		for (int i=0; i<status.length; i++) {
			status[i] = "";
		}
		tanggal = "";
		tanggalShort = "";
		hari = "";
		location = new LatLng(0, 0);
		readFromParcel(p);
	}
	
	
	
	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public static void initLocation() {
		
	}
	
	public static ArrayList<DataPintuAir> checkLocation(LatLng location) {
		ArrayList<DataPintuAir> listPintuAir = new ArrayList<DataPintuAir>();
		
		for (String strPintuAir : mapsPintuAir.keySet()) {
			DataPintuAir cur = mapsPintuAir.get(strPintuAir);
			if (isInArea(cur.getLocation(), location)) {
				listPintuAir.add(cur);
			}
		}
		
		return listPintuAir;
	}
	
	private static boolean isInArea(LatLng location, LatLng currentlocation) {
		// TODO Auto-generated method stub
		float radius = 4000.0f;
		float[] results = new float[1];
		Location.distanceBetween(location.latitude, 
				location.longitude, currentlocation.latitude, currentlocation.longitude, results);
		
		float distance = results[0];
		
		return distance < radius;
	}

	public void setTanggal(String tanggal) {
		String[] arrbulan = new String[] {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
		int bulan = Integer.parseInt(tanggal.split("/")[1]) - 1;
		String strBulan = arrbulan[bulan];
		this.tanggal = tanggal.split("/")[2] + " " + strBulan + " " + tanggal.split("/")[0];
		
		int y = Integer.parseInt(tanggal.split("/")[0]);
		int d = Integer.parseInt(tanggal.split("/")[2]);
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(y, bulan, d);
		
		int h = calendar.get(Calendar.DAY_OF_WEEK);
		hari = hariArr[h-1];
		
		tanggalShort = tanggal.split("/")[2] + "/" + tanggal.split("/")[1];
	}
	public void addTinggiAir(int tinggi, String strStatus, int waktu) {
		tinggiAir[jumlahStatus] = tinggi;
		this.waktu[jumlahStatus] = waktu;
		status[jumlahStatus++] = strStatus;
	}
	
	public String getRentangWaktu() {
		int lastWaktu = getWaktuTerakhir();
		int awalWaktu = lastWaktu - 6;
		
		return String.format("%02d", awalWaktu) + ".00 - " + String.format("%02d", lastWaktu) + ".00 WIB";
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
	
	public int[] getWaktu() {
		return waktu;
	}

	public int getWaktuTerakhir() {
		return waktu[0];
	}

	public int[] getTinggiAir() {
		return tinggiAir;
	}

	public void setTinggiAir(int[] tinggiAir) {
		this.tinggiAir = tinggiAir;
	}

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public int getJumlahStatus() {
		return jumlahStatus;
	}

	public void setJumlahStatus(int jumlahStatus) {
		this.jumlahStatus = jumlahStatus;
	}
	
	public String toString() {
		return nama;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(nama);
		dest.writeIntArray(waktu);
		dest.writeStringArray(status);
		dest.writeIntArray(tinggiAir);
		dest.writeInt(jumlahStatus);
		dest.writeString(tanggal);
		dest.writeString(tanggalShort);
		dest.writeString(hari);
	}
	
	private void readFromParcel(Parcel in) {
		this.nama = in.readString();
		in.readIntArray(this.waktu);
		in.readStringArray(this.status);
		in.readIntArray(this.tinggiAir);
		this.jumlahStatus = in.readInt();
		this.tanggal = in.readString();
		this.tanggalShort = in.readString();
		this.hari = in.readString();
		
		LatLng temp = locationPintuAir.get(this.nama);
		location = temp;
	}

	public static final Parcelable.Creator<DataPintuAir> CREATOR = 
			new Parcelable.Creator<DataPintuAir>() {
				public DataPintuAir createFromParcel(Parcel p) {
					return new DataPintuAir(p);
				}

				@Override
				public DataPintuAir[] newArray(int size) {
					// TODO Auto-generated method stub
					return new DataPintuAir[size];
				}
				
			};
	public String getTanggal() {
		// TODO Auto-generated method stub
		return tanggal;
	}
	
	public String getHari() {
		return hari;
	}
	
	public String getTanggalShort() {
		return tanggalShort;
	}

	@Override
	public int compareTo(DataPintuAir another) {
		// TODO Auto-generated method stub
		if (statusCode.get(status[0]) == statusCode.get(another.status[0])) {
			return this.getNama().compareTo(another.getNama());
		} else {
			return statusCode.get(status[0]) - statusCode.get(another.status[0]);
		}
	}
	
	
	
}
