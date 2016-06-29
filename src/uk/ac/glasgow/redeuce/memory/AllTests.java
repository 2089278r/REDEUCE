package uk.ac.glasgow.redeuce.memory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SingleRegisterTest.class , DoubleRegisterTest.class, QuadRegisterTest.class, DelayLineTest.class })
public class AllTests {

}
