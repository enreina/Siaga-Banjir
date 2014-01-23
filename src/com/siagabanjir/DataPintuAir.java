package com.siagabanjir;

import android.os.Parcel;
import android.os.Parcelable;

public class DataPintuAir implements Parcelable {
	private String nama;
	private int[] tinggiAir;
	private int[] waktu;
	private String[] status;
	private int jumlahStatus;
	private String tanggal;
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
	}
	
	public DataPintuAir(Parcel p) {
		this.waktu = new int[7];
		this.tinggiAir = new int[7];
		this.status = new String[7];
		for (int i=0; i<status.length; i++) {
			status[i] = "";
		}
		tanggal = "";
		readFromParcel(p);
	}
	
	public void setTanggal(String tanggal) {
		String[] arrbulan = new String[] {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
		int bulan = Integer.parseInt(tanggal.split("/")[1]) - 1;
		String strBulan = arrbulan[bulan];
		this.tanggal = tanggal.split("/")[2] + " " + strBulan + " " + tanggal.split("/")[0];
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
	}
	
	private void readFromParcel(Parcel in) {
		this.nama = in.readString();
		in.readIntArray(this.waktu);
		in.readStringArray(this.status);
		in.readIntArray(this.tinggiAir);
		this.jumlahStatus = in.readInt();
		this.tanggal = in.readString();
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
	
	
	
	
}
