<?php
include("simple_html_dom.php");

//get current date info
//current year
$current_year = date("Y");
$current_month = date("m");
$current_day = date("d");
$current_ampm = 0;

if (date("a") == "pm") {
	$current_ampm = 1;
}

$waktu = 12;
$lastwaktu = 12;
$num = 1;

$url1 = "http://www.jakarta.go.id/web/data_pantauan/search/".$current_year."/".$current_month."/".$current_day."/".$current_ampm;
$url2 = "";
if ($current_ampm == 1) {
	$url2 = "http://www.jakarta.go.id/web/data_pantauan/search/".$current_year."/".$current_month."/".$current_day."/0";
} else {
	$yesterday_year = date("Y", strtotime("-1 day"));
	$yesterday_month = date("m", strtotime("-1 day"));
	$yesterday_day = date("d", strtotime("-1 day"));
	
	$url2 = "http://www.jakarta.go.id/web/data_pantauan/search/".$yesterday_year."/".$yesterday_month."/".$yesterday_day."/1";
}


//fetch html from jakarta.go.id
$html1 = file_get_html($url1);
$html2 = file_get_html($url2);

$table1 = $html1->find('table[class=tbl1]', 0);
$data = $table1->first_child();
$table2 = $html2->find('table[class=tbl1]', 0);
$data2 = $table2->first_child();

if ($data != null && $data2 != null) {
	$data = $data->next_sibling();
	$data2 = $data2->next_sibling();
}

echo "{ \"datapintuair\": [";
while ($data != null && $data2 != null) {
	
	$tanggal = $current_year."/".$current_month."/".$current_day;
	$nama = $data->find('td', 1);
	$nama = $nama->plaintext;
	$nama = trim($nama);
	
	$status[0] = "NORMAL";
	$waktu = $current_ampm == 0 ? 12 : 24;
	$cell = $data->last_child();
	$tinggi[0] = $cell->plaintext;
	$tinggi[0] = trim($tinggi[0]);
	$waktutinggi[0] = $current_ampm == 0 ? 12 : 24;
	
	while ($tinggi[0] == "-" && $waktu > ($current_ampm == 0 ? 1 : 13) ) {
		$waktu--;
		$cell = $cell->prev_sibling();
		$tinggi[0] = $cell->plaintext;
		$tinggi[0] = trim($tinggi[0]);
	}
	
	
	if ($tinggi[0] == "-") {
		$waktu = ($current_ampm == 1) ? 12 : 24;
		$cell = $data2->last_child();
		$tinggi[0] = $cell->plaintext;
		$tinggi[0] = trim($tinggi[0]);
		
		while ($tinggi[0] == "-" && $waktu > ($current_ampm == 1 ? 1 : 13)) {
			$waktu--;
			$cell = $cell->prev_sibling();
			$tinggi[0] = $cell->plaintext;
			$tinggi[0] = trim($tinggi[0]);
		}
		if ($tinggi[0] != "-") {
			$font = $cell->find('font', 0);
			if ($font->color == "#AA8F5F") {
				$status[0] = "WASPADA";
			} else if ($font->color == "#FFC917") {
				$status[0] = "RAWAN";
			} else if ($font->color == "#FF2503") {
				$status[0] = "KRITIS";
			}
			if ($current_ampm == 0) {
				$tanggal = $yesterday_year."/".$yesterday_month."/".$yesterday_day;
			}
		}
		
		$waktutinggi[0] = $waktu;
		
		$num = 1;
		$lastwaktu = $waktu;
		while ($num < 7  && $lastwaktu > ($current_ampm == 1 ? 1 : 13)) {
			$lastwaktu--;
			$cell = $cell->prev_sibling();
			$tinggi[$num] = $cell->plaintext;
			$tinggi[$num] = trim($tinggi[1]);
			$font = $cell->find('font', 0);
			$status[$num] = "NORMAL";
			if ($font->color == "#AA8F5F") {
				$status[$num] = "WASPADA";
			} else if ($font->color == "#FFC917") {
				$status[$num] = "RAWAN";
			} else if ($font->color == "#FF2503") {
				$status[$num] = "KRITIS";
			}
			$waktutinggi[$num] = $lastwaktu;
			$num++;
		}
	} else {
		$font = $cell->find('font', 0);
		if ($font->color == "#AA8F5F") {
			$status[0] = "WASPADA";
		} else if ($font->color == "#FFC917") {
			$status[0] = "RAWAN";
		} else if ($font->color == "#FF2503") {
			$status[0] = "KRITIS";
		}
		
		$waktutinggi[0] = $waktu;
		
		$num = 1;
		$lastwaktu = $waktu;
		echo "num: $num, lastwaktu: $lastwaktu";
		while ($num < 7  && $lastwaktu > ($current_ampm == 0 ? 1 : 13)) {
			echo "loop..";
			$lastwaktu--;
			$cell = $cell->prev_sibling();
			$tinggi[$num] = $cell->plaintext;
			if ($tinggi[$num] == "-") {
				continue;
			}
			$tinggi[$num] = trim($tinggi[$num]);
			$font = $cell->find('font', 0);
			$status[$num] = "NORMAL";
			if ($font->color == "#AA8F5F") {
				$status[$num] = "WASPADA";
			} else if ($font->color == "#FFC917") {
				$status[$num] = "RAWAN";
			} else if ($font->color == "#FF2503") {
				$status[$num] = "KRITIS";
			}
			$waktutinggi[$num] = $lastwaktu;
			$num++;
		}
		
	}
	echo "{\"nama\": \"$nama\", \"tanggal\": \"$tanggal\", \"tinggiair\": [";
	for ($i=0; $i < count($tinggi); $i++) {
		echo "{\"tinggi\" : \"".$tinggi[$i]."\", \"status\": \"".$status[$i]."\", \"waktu\": ".$waktutinggi[$i]."}";
		if ($i < count($tinggi)-1) echo ", ";
	}
	echo "]}";
	
	
	$data = $data->next_sibling();
	$data2 = $data2->next_sibling();
	if ($data != null && $data2 != null) {
		echo ", ";
	}
}
echo "], \"tanggal\": \"$tanggal\"}";
?>