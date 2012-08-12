<?php include('header.php'); ?>
<div class="row">
<div class="twelve columns">
<hr />

<h3>Data</h3>

<table id="filterTable1">
<thead>
	<th>Date-Time</th>
    <th>Email</th>
    <th>Message</th>
    <th>Area</th>
    <th>Model</th>
    <th>Android</th>
</thead>

<tfoot></tfoot>

<tbody>

<?php require('config.php');

$result = mysql_query("SELECT * FROM data");

while($row = mysql_fetch_array($result))
{
    echo "<tr>";
	echo "<td>".$row['TIME']."</td>";
    echo "<td>".$row['EMAIL']."</td>";
	echo "<td>".$row['MESSAGE']."</td>";
	echo "<td>".$row['AREA']."</td>";
	echo "<td>".$row['MODEL']."</td>";
    echo "<td>".$row['ANDROID_VER']."</td>";
    echo "<tr>";
}

mysql_close($con);
?>

</tbody>
</table>
</div> <!-- /div.row -->
</div> <!-- /div.twelve columns -->
<?php include('footer.php'); ?>
