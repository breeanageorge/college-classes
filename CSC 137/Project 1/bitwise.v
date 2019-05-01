module bitwise(x, y, z, f);
	input x, y, z;
	output f;

	wire x, y, z, f;

	assign f = ((x&~y)|(y&z));
	
endmodule