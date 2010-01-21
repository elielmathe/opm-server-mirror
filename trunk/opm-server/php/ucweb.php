<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
	if (function_exists("curl_init")) {
		if (!$_GET["test"] != null) {
			header( "HTTP/1.1 301 Moved Permanently" );
			header( "Location: http://www.google.com/" );
		} else {
			echo 'Hello UCWeb Server! Fuck GFW!';
		}
	} else {
		echo 'cURL is not enabled.';
	}
} else {
	$curlInterface = curl_init();
	$headers[] = 'Content-Type", "text/xml';
	$headers[] = 'User-Agent", "Java/1.6.0_15';
	$headers[] = 'Connection", "keep-alive';
	curl_setopt_array($curlInterface, array(
		CURLOPT_URL => 'http://uc.ucweb.com',
		CURLOPT_HTTPHEADER => $headers,
		CURLOPT_POST => 1,
		CURLOPT_POSTFIELDS => @file_get_contents('php://input'))
    );
	$result = curl_exec($curlInterface);
	curl_close($curlInterface);
	echo $result;
}
?>
