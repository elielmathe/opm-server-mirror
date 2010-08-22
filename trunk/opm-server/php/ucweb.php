<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    if (!$_GET["test"] != null) {
        header( "HTTP/1.1 301 Moved Permanently" );
        header( "Location: http://www.google.com/" );
    } else {
        echo 'Hello UCWeb Server! Fuck GFW!';
    }
} else {
    $servers = array("75.126.123.205:8086",
                     "75.126.123.206:8089",
                     "75.126.123.208:8089",
                     "75.126.123.209:8090",
                     "75.126.123.210:8087",
                     "75.126.123.211:8089",
                     "75.126.123.212:8087");	
    //$server = "67.228.68.101:8086";
    $server = $servers[rand(0, 6)];
    header("Assign: ".$server);
    echo "\x00\x06assign\x00\x13".$server;
}
?>
