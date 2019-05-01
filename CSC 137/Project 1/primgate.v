module primgate(x, y, z, f, nandout1, nandout2, notout1);
	input x, y, z;
	output f;
	inout nandout1, nandout2, notout1;
	
	wire x, y, z, f, nandout1, nandout2, notout1;
	
	not noty(notout1, y);
	
	nand nand1(nandout1,y,z);
	nand nand2(nandout2,notout1,x);
	nand nand3(f,nandout1,nandout2);
	
endmodule
