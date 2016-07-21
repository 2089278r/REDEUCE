package uk.ac.glasgow.redeuce.memory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import uk.ac.glasgow.redeuce.peripherals.memory.CRDFileReaderTest;
import uk.ac.glasgow.redeuce.peripherals.memory.DEUCECardReaderTest;
import uk.ac.glasgow.redeuce.peripherals.memory.TriadTest;
import uk.ac.glasgow.redeuce.processor.InstructionTest;
import uk.ac.glasgow.redeuce.processor.ProcessorTest;

@RunWith(Suite.class)
@SuiteClasses({ SingleRegisterTest.class , DoubleRegisterTest.class, QuadRegisterTest.class, DelayLineTest.class, 
	MemoryTest.class, InstructionWordTest.class, WordTest.class, CRDFileReaderTest.class, DEUCECardReaderTest.class, 
	TriadTest.class, InstructionTest.class, ProcessorTest.class})
public class AllTests {

}
