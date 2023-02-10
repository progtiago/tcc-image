package br.ufpr.tcc.image.support;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class TestSupport {

  @Before
  public abstract void init();

}