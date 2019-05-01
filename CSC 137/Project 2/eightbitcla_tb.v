`include "eightbitcla.v"
module eightbitcla_tb();
	reg a, b, cin;
	
	reg [7:0] k;
	wire cout, sout;
	
	eightbitcla eightbitcla_test(a, b, cin, cout, sout);
	
	initial begin
	$display("Time a b cin cout sout"); //header
	$monitor ("%4d       %b", $time, cout, sout);
	
	for(k=0; k<=7; k=k+1) begin
		#1 a=k[2];b=k[1];cin=k[0]; 
		$display("%4d %b %b", $time, cout, sout);
	end
	#10 $finish; //stops simulation
	end

endmodule
