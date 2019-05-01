module moore_seq(
input clock, reset, x,
output z
);

	reg q0, q1, q2;
	wire d2, d1, d0;

	assign d2 = (~x & ~q2 & q1 & ~q0) | (x & q2 & ~q1 & ~q0);
	assign d1 = ~x & ~q1 & q0;
	assign d0 = (x & ~q1) | (x & ~q2 & ~q0);
	
	
	
	always@(posedge clock, posedge reset)
		if (reset == 1) begin
		q0 <= 1'b0;
		q1 <= 1'b0;
		q2 <= 1'b0;
		end
		else begin
		q0 <= d0;
		q1 <= d1;
		q2 <= d2;
		end
	
	assign z = q2 & ~q1 & q0;
	
endmodule