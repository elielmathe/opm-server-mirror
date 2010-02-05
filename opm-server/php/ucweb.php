<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    if (!$_GET["test"] != null) {
        header( "HTTP/1.1 301 Moved Permanently" );
        header( "Location: http://www.google.com/" );
    } else {
        echo 'Hello UCWeb Server! Fuck GFW!';
    }
} else {
    $servers = array("67.228.68.101:8086",
                     "67.228.166.110:8089",
                     "67.228.166.103:8089",
                     "67.228.166.108:8090",
                     "74.86.222.70:8087",
                     "74.86.222.73:8089",
                     "74.86.222.81:8087");	
    //$server = "67.228.68.101:8086";
    $server = $servers[rand(0, 6)];
    header("Assign: ".$server);
    echo "\x00\x06assign\x00\x13".$server;
}
?>
