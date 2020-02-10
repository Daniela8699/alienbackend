package icesi.vip.alien.alien;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.xml.SimpleSaxErrorHandler;

import icesi.vip.alien.alien.simplexMethod.SensivilityAnalysis;
import icesi.vip.alien.alien.simplexMethod.Simplex;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimplexTests {

		private Simplex simplex;
		
		public void setupScenarioNormal() {
			
			try {
				simplex = new Simplex("MAXIMIZE", new String[] {"1 Z -3 X1 -5 X2 = 0",
						 "0 Z 1 X1 0 X2 <= 4",
						 "0 Z 0 X1 2 X2 <= 12",
						 "0 Z 3 X1 2 X2 <= 18"});
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}
		}
		
		public void setupScenarioBigM() {
			
			try {
				simplex = new Simplex("MINIMIZE", new String[] { "1 Z -2 X1 -3 X2 = 0",
							"0 Z 0.5 X1 0.25 X2 <= 4",
							"0 Z 1 X1 3 X2 >= 20",
							"0 Z 1 X1 1 X2 = 10" });
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}
		}
		
		public void setupScenarioNotFeasible() {
			
			try {
				simplex = new Simplex("MINIMIZE", new String[] {"1 Z -2 X1 -3 X2 = 0",
						 "0 Z 0.5 X1 0.25 X2 <= 4",
						 "0 Z 1 X1 3 X2 >= 36",
						 "0 Z 1 X1 1 X2 = 10"});
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}
		}

		public void setupScenarioNotBounded() {
	
			try {
				simplex = new Simplex("MAXIMIZE", new String[] {"1 Z -36 X1 -30 X2 3 X3 4 X4 = 0",
								 "0 Z 1 X1 1 X2 -1 X3 0 X4 <= 5",
								 "0 Z 6 X1 5 X2 0 X3 -1 X4 <= 10"});
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}	
		}	
		
		public void setupScenarioInfiniteSol() {
			
			try {
				simplex = new Simplex("MAXIMIZE", new String[] {"1 Z -60 X1 -35 X2 -20 X3 = 0",
						 "0 Z 8 X1 6 X2 1 X3 <= 48",
						 "0 Z 4 X1 2 X2 1.5 X3 <= 20",
						 "0 Z 2 X1 1.5 X2 0.5 X3 <= 8",
						 "0 Z 0 X1 1 X2 0 X3 <= 5"});
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}	
		}	
		
		public void setupScenarioNormal1() {
			
			try {
				simplex = new Simplex("MAXIMIZE", new String[] { "1 Z -2 X1 4 X2 -5 X3 6 X4 = 0",
						"0 Z 1 X1 4 X2 -2 X3 0 X4 <= 2",
						"0 Z -1 X1 2 X2 3 X3 4 X4 <= 1"});
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}	
		}	
		
		public void setupScenarioNormal2() {
			
			try {
				simplex = new Simplex("MAXIMIZE", new String[] { "1 Z -3 X1 -2 X2 -5 X3 = 0",
						"0 Z 1 X1 2 X2 1 X3 <= 430",
						"0 Z 3 X1 0 X2 2 X3 <= 460",
						"0 Z 1 X1 4 X2 0 X3 <= 420" });
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}	
		}	

		public void setupScenarioMonster() {
			
			try {
				simplex = new Simplex("MAXIMIZE", new String[] { "1 Z -14 X1 -20 X2 0 X3 0 X4 = 0",
						"0 Z 0 X1 0 X2 8 X3 8 X4 = 1",
						"0 Z 14 X1 20 X2 -8 X3 -8 X4 <= 0",
						"0 Z 25 X1 42 X2 -11 X3 -15 X4 <= 0",
						"0 Z 8 X1 30 X2 -14 X3 -12 X4 <= 0",
						"0 Z 25 X1 8 X2 -12 X3 -13 X4 <= 0",
						"0 Z 40 X1 22 X2 -11 X3 -18 X4 <= 0",
						"0 Z 24 X1 30 X2 -18 X3 -20 X4 <= 0",
						"0 Z 1 X1 0 X2 0 X3 0 X4 >= 0.00001",
						"0 Z 0 X1 1 X2 0 X3 0 X4 >= 0.00001",
						"0 Z 0 X1 0 X2 1 X3 0 X4 >= 0.00001",
						"0 Z 0 X1 0 X2 0 X3 1 X4 >= 0.00001"});
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}	
		}
		
		@Test
		public void stepByStepTestNormal() {
			setupScenarioNormal();
			double[][] expected = {{-3, -5, 0, 0, 0, 0},
					{1, 0, 1, 0, 0, 4},
					{0, 2, 0, 1, 0, 12},
					{3, 2, 0, 0, 1, 18}};
			assertEquals(expected, simplex.getActualMatrix());
			simplex.nextIteration();
					expected = new double[][] 	  {{-3, 0, 0, 2.5, 0, 30},
													{1, 0, 1, 0, 0, 4},
													{0, 1, 0, 0.5, 0, 6},
													{3, 0, 0, -1, 1, 6}};
				assertEquals(expected, simplex.getActualMatrix());
				simplex.nextIteration();
					expected = new double[][] {{0, 0, 0, 1.5, 1, 36},
												{0, 0, 1, 0.33333, -0.33333, 2},
												{0, 1, 0, 0.5, 0, 6},
												{1, 0, 0, -0.33333, 0.33333, 2}};
								expected = simplex.roundMatrix(expected);
						assertEquals(expected, simplex.getActualMatrix());
						simplex.nextIteration();
						assertEquals(Simplex.SOLVED, simplex.getMessageSol());
		}
		
		@Test
		public void stepByStepTestBigM() {
			setupScenarioBigM();
			double[][] expected = {{1999998, 3999997, 0, 0, 0, -1000000, 30000000},
									{0.5, 0.25, 1, 0, 0, 0, 4},
									{1, 3, 0, 1, 0, -1, 20},
									{1, 1, 0, 0, 1, 0, 10}};
			assertEquals(expected, simplex.getActualMatrix());
			simplex.nextIteration();
					expected = new double[][] {{666665.66666, 0, 0, -1333332.33333, 0, 333332.33333, 3333353.33333},
										{0.417, 0, 1, -0.083, 0, 0.083, 2.33333},
										{0.33333, 1, 0, 0.33333, 0, -0.33333, 6.6666},
										{0.667, 0, 0, -0.33333, 1, 0.33333, 3.33333}};
								expected = Simplex.roundMatrix(expected);
				assertEquals(expected, simplex.getActualMatrix());
				simplex.nextIteration();
					expected = new double[][] {{0, 0, 0, -999999.5, -999998.5, -0.5, 25},
										{0, 0, 1, 0.125, -0.625, -0.125, 0.25},
										{0, 1, 0, 0.5, -0.5, -0.5, 5},
										{1, 0, 0, -0.5, 1.5, 0.5, 5}};
								expected = simplex.roundMatrix(expected);
						assertEquals(expected, simplex.getActualMatrix());
						simplex.nextIteration();
						assertEquals(Simplex.SOLVED, simplex.getMessageSol());
		}
		
		@Test
		public void stepByStepTestNotFeasible() {
			setupScenarioNotFeasible();
			double[][] expected = {{1999998, 3999997, 0, 0, 0, -1000000, 46000000},
					{0.5, 0.25, 1, 0, 0, 0, 4},
							{1, 3, 0, 1, 0, -1, 36},
							{1, 1, 0, 0, 1, 0, 10}};
			assertEquals(expected, simplex.getActualMatrix());
			simplex.nextIteration();
					expected = new double[][] {{-1999999, 0, 0, 0, -3999997, -1000000, 6000030},
										{0.25, 0, 1, 0, -0.25, 0, 1.5},
										{-2, 0, 0, 1, -3, -1, 6},
										{1, 1, 0, 0, 1, 0, 10}};
								expected = Simplex.roundMatrix(expected);
				assertEquals(expected, simplex.getActualMatrix());
				simplex.nextIteration();
						assertEquals(Simplex.NOT_FEASIBLE, simplex.getMessageSol());
		}
		
		@Test
		public void stepByStepTestNotBounded() {
			setupScenarioNotBounded();
			double[][] expected = {{-36, -30, 3, 4, 0, 0, 0},
								{1, 1, -1, 0, 1, 0, 5},
								{6, 5, 0, -1, 0, 1, 10}};
			assertEquals(expected, simplex.getActualMatrix());
			simplex.nextIteration();
					expected = new double[][] {{0, 0, 3, -2, 0, 6, 60},
									{0, 0.167, -1, 0.167, 1, -0.167, 3.33333},
									{1, 0.833, 0, -0.167, 0, 0.167, 1.6666}};
								expected = Simplex.roundMatrix(expected);
				assertEquals(expected, simplex.getActualMatrix());
				simplex.nextIteration();
					expected = new double[][] {{0, 2, -9, 0, 12, 4, 100},
										{0, 1, -6, 1, 6, -1, 20},
										{1, 1, -1, 0, 1, 0, 5}};
								expected = simplex.roundMatrix(expected);
						assertEquals(expected, simplex.getActualMatrix());
						simplex.nextIteration();
						assertEquals(Simplex.NOT_BOUNDED, simplex.getMessageSol());
		}
		
		@Test
		public void stepByStepTestInfiniteSol() {
			setupScenarioInfiniteSol();
			double[][] expected = {{-60, -35, -20, 0, 0, 0, 0, 0},
								{8, 6, 1, 1, 0, 0, 0, 48},
								{4, 2, 1.5, 0, 1, 0, 0, 20},
								{2, 1.5, 0.5, 0, 0, 1, 0, 8},
								{0, 1, 0, 0, 0, 0, 1, 5}};
			assertEquals(expected, simplex.getActualMatrix());
			simplex.nextIteration();
					expected = new double[][] {{0, 10, -5, 0, 0, 30, 0, 240},
											{0, 0, -1, 1, 0, -4, 0, 16},
											{0, -1, 0.5, 0, 1, -2, 0, 4},
											{1, 0.75, 0.25, 0, 0, 0.5, 0, 4},
											{0, 1, 0, 0, 0, 0, 1, 5}};
								expected = Simplex.roundMatrix(expected);
				assertEquals(expected, simplex.getActualMatrix());
				simplex.nextIteration();
					expected = new double[][] {{0, 0, 0, 0, 10, 10, 0, 280},
											{0, -2, 0, 1, 2, -8, 0, 24},
											{0, -2, 1, 0, 2, -4, 0, 8},
											{1, 1.25, 0, 0, -0.5, 1.5, 0, 2},
											{0, 1, 0, 0, 0, 0, 1, 5}};
								expected = simplex.roundMatrix(expected);
						assertEquals(expected, simplex.getActualMatrix());
						simplex.nextIteration();
						assertEquals(Simplex.INFINITE_SOLUTIONS, simplex.getMessageSol());
		}
		
		@Test
		public void stepByStepTestNormal1() {
			setupScenarioNormal1();
			double[][] expected = {{-2, 4, -5, 6, 0, 0, 0},
								{1, 4, -2, 0, 1, 0, 2},
								{-1, 2, 3, 4, 0, 1, 1}};
			assertEquals(expected, simplex.getActualMatrix());
			simplex.nextIteration();
					expected = new double[][] {{-3.66666, 7.33333, 0, 12.66666, 0, 1.66666, 1.66666},
										{0.33333, 5.33333, 0, 2.66666, 1, 0.66666, 2.66666},
										{-0.33333, 0.66666, 1, 1.33333, 0, 0.33333, 0.33333}};
								expected = Simplex.roundMatrix(expected);
				assertEquals(expected, simplex.getActualMatrix());
				simplex.nextIteration();
					expected = new double[][] {{0, 66, 0, 42, 11, 9, 31},
											{1, 16, 0, 8, 3, 2, 8},
											{0, 6, 1, 4, 1, 1, 3}};
								expected = simplex.roundMatrix(expected);
						assertEquals(expected, simplex.getActualMatrix());
						simplex.nextIteration();
						assertEquals(Simplex.SOLVED, simplex.getMessageSol());
		}
		
		@Test
		public void stepByStepTestNormal2() {
			setupScenarioNormal2();
			double[][] expected = {{-3, -2, -5, 0, 0, 0, 0},
									{1, 2, 1, 1, 0, 0, 430},
									{3, 0, 2, 0, 1, 0, 460},
									{1, 4, 0, 0, 0, 1, 420}};
			assertEquals(expected, simplex.getActualMatrix());
			simplex.nextIteration();
					expected = new double[][] {{4.5, -2, 0, 0, 2.5, 0, 1150},
											{-0.5, 2, 0, 1, -0.5, 0, 200},
											{1.5, 0, 1, 0, 0.5, 0, 230},
											{1, 4, 0, 0, 0, 1, 420}};
								expected = Simplex.roundMatrix(expected);
				assertEquals(expected, simplex.getActualMatrix());
				simplex.nextIteration();
					expected = new double[][] {{4, 0, 0, 1, 2, 0, 1350},
										{-0.25, 1, 0, 0.5, -0.25, 0, 100},
										{1.5, 0, 1, 0, 0.5, 0, 230},
										{2, 0, 0, -2, 1, 1, 20}};
								expected = simplex.roundMatrix(expected);
						assertEquals(expected, simplex.getActualMatrix());
						simplex.nextIteration();
						assertEquals(Simplex.SOLVED, simplex.getMessageSol());
		}
		
		@Test
		public void sensitivityA1() {
			setupScenarioNormal2();
			simplex.nextIteration();
			simplex.nextIteration();
			simplex.nextIteration();
			simplex.buildAnalysis();
			simplex.getIntervals();
			SensivilityAnalysis sA = simplex.getAnalysis();
			double [][] interDCons = new double[][] {{200, 10},
													{20, 400},
													{20, Double.MAX_VALUE}};
			double [][] interDFO = new double[][] {{Double.MAX_VALUE, 4},
													{2, 8},
													{simplex.roundDouble(2.66666), Double.MAX_VALUE}};										
			assertEquals(sA.getIntervalsDConstraints(), interDCons);
			assertEquals(sA.getIntervalsDFO(), interDFO);
			assertEquals(sA.getShadowPrice(), new double[][] {{1, 2, 0}});
			double[] exp = simplex.getReducedCosts();
			double[] real = new double[] {4, 0, 0};//-4, 0, 0
			for (int i = 0; i < exp.length; i++) {
			assertTrue(exp[i]== real[i]);
			}
		}
		@Test
		public void sensitivityA2() {
			setupScenarioNormal1();
			simplex.nextIteration();
			simplex.nextIteration();
			simplex.nextIteration();
			simplex.buildAnalysis();
			simplex.getIntervals();
			SensivilityAnalysis sA = simplex.getAnalysis();
			double [][] interDCons = new double[][] {{simplex.roundDouble(2.66666), Double.MAX_VALUE},
													{3, Double.MAX_VALUE}};
			double [][] interDFO = new double[][] {{simplex.roundDouble(3.66666), Double.MAX_VALUE},
													{Double.MAX_VALUE, 66},
													{9, Double.MAX_VALUE},
													{Double.MAX_VALUE, 42}};										
			assertEquals(sA.getIntervalsDConstraints(), interDCons);
			assertEquals(sA.getIntervalsDFO(), interDFO);
			assertEquals(sA.getShadowPrice(), new double[][] {{11, 9}});
			double[] exp = simplex.getReducedCosts();
			double[] real = new double[] {0, 66, 0, 42}; //0, -66, 0, -42
			for (int i = 0; i < exp.length; i++) {
			assertTrue(exp[i]== real[i]);
			}
		}
		
		@Test
		public void sensitivityA3() {
			setupScenarioBigM();
			simplex.nextIteration();
			simplex.nextIteration();
			simplex.nextIteration();
			simplex.buildAnalysis();
			simplex.getIntervals();
			SensivilityAnalysis sA = simplex.getAnalysis();
			double [][] interDCons = new double[][] {{0.25, Double.MAX_VALUE},
													{2, 10},
													{3.33333, 0.4}};
			double [][] interDFO = new double[][] {{Double.MAX_VALUE, 1},
													{1, Double.MAX_VALUE}};
													
			assertEquals(sA.getIntervalsDConstraints(), simplex.roundMatrix(interDCons));
			assertEquals(sA.getIntervalsDFO(), interDFO);
			assertEquals(sA.getShadowPrice(), new double[][] {{0, 0.5, 1.5}});
			double[] exp = simplex.getReducedCosts();
			double[] real = new double[] {0, 0};
			for (int i = 0; i < exp.length; i++) {
			System.out.println("fuck" + exp[i]);
			assertTrue(exp[i]== real[i]);
			}
		}
		
		@Test
		public void sensitivityA4() {
			setupScenarioMonster();
			while(simplex.getMessageSol() == null)
			simplex.nextIteration();
			simplex.buildAnalysis();
			simplex.getIntervals();
			SensivilityAnalysis sA = simplex.getAnalysis();
			double [][] interDCons = new double[][] {{0.99940533, Double.MAX_VALUE},
													{0.00842, Double.MAX_VALUE},
													{0.46847, 0.01936},
													{0.62525, Double.MAX_VALUE},
													{0.31419, Double.MAX_VALUE},
													{1.26753, 0.10824},
													{0.87125, Double.MAX_VALUE},
													{Double.MAX_VALUE, 0.04711},
													{Double.MAX_VALUE, 0.01658},
													{0, 0.12498},
													{Double.MAX_VALUE, 0.12498}};
//			interDCons = simplex.roundMatrix(interDCons);
//			double [][] interCExp = sA.getIntervalsDConstraints();
			
			double [][] interDFO = new double[][] {{2.09523, 22.36363},
													{12.3, 3.52},
													{Double.MAX_VALUE, 2.28672},
													{2.28672, Double.MAX_VALUE}};
//						for (int i = 0; i < interDCons.length; i++) {
//							for (int j = 0; j < interDCons[0].length; j++) {
//								System.out.println("coge" + interCExp[i][j]);
//								System.out.println("coge" + interDCons[i][j]);
//								
//								assertTrue(interDCons[i][j]== interCExp[i][j]);
//							}
//						}					
			assertEquals(sA.getIntervalsDConstraints(), simplex.roundMatrix(interDCons));
			assertEquals(sA.getIntervalsDFO(), simplex.roundMatrix(interDFO));
			assertEquals(sA.getShadowPrice(), simplex.roundMatrix(new double[][] {{0.99159, 0, 0.43539, 0, 0, 0.07787, 0, 0, 0, -2.28672, 0}}));
			double[] exp = simplex.getReducedCosts();
			double[] real = new double[] {0, 0, 0, 0};
			for (int i = 0; i < real.length; i++) {
			assertTrue(exp[i]== real[i]);
			}
		}
}
