/*
 * Copyright (c) 2013, European Bioinformatics Institute (EMBL-EBI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package uk.ac.ebi.grins;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** @author John May */
public class ChemicalGraphTest {

    @Test public void addAtoms() {
        ChemicalGraph g = new ChemicalGraph(5);
        assertThat(g.addAtom(mock(Atom.class)), is(0));
        assertThat(g.addAtom(mock(Atom.class)), is(1));
        assertThat(g.addAtom(mock(Atom.class)), is(2));
        assertThat(g.addAtom(mock(Atom.class)), is(3));
        assertThat(g.addAtom(mock(Atom.class)), is(4));
    }

    @Test public void addAtomsResize() {
        ChemicalGraph g = new ChemicalGraph(2);
        assertThat(g.addAtom(mock(Atom.class)), is(0));
        assertThat(g.addAtom(mock(Atom.class)), is(1));
        assertThat(g.addAtom(mock(Atom.class)), is(2));
        assertThat(g.addAtom(mock(Atom.class)), is(3));
        assertThat(g.addAtom(mock(Atom.class)), is(4));
    }

    @Test public void atomAccess() {
        Atom[] atoms = new Atom[]{
                mock(Atom.class),
                mock(Atom.class),
                mock(Atom.class),
                mock(Atom.class)
        };
        ChemicalGraph g = new ChemicalGraph(5);
        for (Atom a : atoms)
            g.addAtom(a);
        assertThat(g.atom(0), is(atoms[0]));
        assertThat(g.atom(1), is(atoms[1]));
        assertThat(g.atom(2), is(atoms[2]));
        assertThat(g.atom(3), is(atoms[3]));
    }

    @Test public void testOrder() {
        ChemicalGraph g = new ChemicalGraph(5);
        assertThat(g.order(), is(0));
        g.addAtom(mock(Atom.class));
        assertThat(g.order(), is(1));
        g.addAtom(mock(Atom.class));
        assertThat(g.order(), is(2));
        g.addAtom(mock(Atom.class));
        assertThat(g.order(), is(3));
        g.addAtom(mock(Atom.class));
        assertThat(g.order(), is(4));
        g.addAtom(mock(Atom.class));
        assertThat(g.order(), is(5));
    }

    @Test public void testSize() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));

        Edge e1 = new Edge(0, 1, Bond.IMPLICIT);
        Edge e2 = new Edge(0, 1, Bond.IMPLICIT);

        assertThat(g.size(), is(0));
        g.addEdge(e1);
        assertThat(g.size(), is(1));
        g.addEdge(e2);
        assertThat(g.size(), is(2));
    }

    @Test public void testEdges() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        assertThat(g.edges(0).size(), is(1));
        assertThat(g.edges(0), hasItem(new Edge(0, 1, Bond.IMPLICIT)));
        assertThat(g.edges(1).size(), is(2));
        assertThat(g.edges(1), hasItems(new Edge(0, 1, Bond.IMPLICIT),
                                        new Edge(1, 0, Bond.IMPLICIT)));
    }

    @Test public void testEdgesResize() {
        ChemicalGraph g = new ChemicalGraph(2);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        assertThat(g.edges(0).size(), is(1));
        assertThat(g.edges(0), hasItem(new Edge(0, 1, Bond.IMPLICIT)));
        assertThat(g.edges(1).size(), is(2));
        assertThat(g.edges(1), hasItems(new Edge(0, 1, Bond.IMPLICIT),
                                        new Edge(1, 0, Bond.IMPLICIT)));
    }

    @Test public void testEdgesIterable() {
        ChemicalGraph g = new ChemicalGraph(2);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));

        Iterable<Edge> es = g.edges();
        Iterator<Edge> it = es.iterator();
        assertTrue(it.hasNext());
        assertThat(it.next(), is(new Edge(0, 1, Bond.IMPLICIT)));
        assertTrue(it.hasNext());
        assertThat(it.next(), is(new Edge(1, 2, Bond.IMPLICIT)));
        assertFalse(it.hasNext());
    }


    @Test public void testDegree() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        assertThat(g.degree(0), is(1));
        assertThat(g.degree(1), is(2));
    }

    @Test public void adjacent() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        assertTrue(g.adjacent(0, 1));
        assertTrue(g.adjacent(1, 2));
        assertFalse(g.adjacent(0, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void adjacentInvalid() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        g.adjacent(0, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void adjacentInvalid2() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        g.adjacent(4, 0);
    }

    @Test
    public void edge() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        assertThat(g.edge(0, 1), is(new Edge(0, 1, Bond.IMPLICIT)));
        assertThat(g.edge(1, 2), is(new Edge(1, 2, Bond.IMPLICIT)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void edgeNone() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        g.edge(0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEdges() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.edges(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDegree() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.degree(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEdgeNonVertex1() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEdgeNonVertex2() {
        ChemicalGraph g = new ChemicalGraph(5);
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(1, 0, Bond.IMPLICIT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidAtom() {
        new ChemicalGraph(5).atom(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidAtom2() {
        new ChemicalGraph(5).atom(2);
    }

    @Test public void addTopology() {
        Topology t = mock(Topology.class);
        when(t.atom()).thenReturn(5);
        ChemicalGraph g = new ChemicalGraph(5);
        g.addTopology(t);
        assertThat(g.topologyOf(5), is(t));
    }

    @Test public void addUnknownTopology() {
        Topology t = Topology.unknown();
        ChemicalGraph g = new ChemicalGraph(5);
        assertFalse(g.addTopology(t));
    }

    @Test public void defaultTopology() {
        ChemicalGraph g = new ChemicalGraph(5);
        assertThat(g.topologyOf(5), is(Topology.unknown()));
    }

    @Test public void clear() {
        ChemicalGraph g = new ChemicalGraph(2);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        assertThat(g.order(), is(3));
        assertThat(g.size(), is(2));
        assertThat(g.edges(0).size(), is(1));
        assertThat(g.edges(0), hasItem(new Edge(0, 1, Bond.IMPLICIT)));
        assertThat(g.edges(1).size(), is(2));
        assertThat(g.edges(1), hasItems(new Edge(0, 1, Bond.IMPLICIT),
                                        new Edge(1, 0, Bond.IMPLICIT)));
        g.clear();
        assertThat(g.order(), is(0));
        assertThat(g.size(), is(0));
    }

    @Test public void permute() {
        ChemicalGraph g = new ChemicalGraph(2);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        g.addEdge(new Edge(2, 3, Bond.IMPLICIT));

        assertThat(g.degree(0), is(1));
        assertThat(g.degree(1), is(2));
        assertThat(g.degree(2), is(2));
        assertThat(g.degree(3), is(1));

        ChemicalGraph h = g.permute(new int[]{1, 0, 3, 2});
        assertThat(h.degree(0), is(2));
        assertThat(h.degree(1), is(1));
        assertThat(h.degree(2), is(1));
        assertThat(h.degree(3), is(2));
        assertThat(g.atom(0), is(h.atom(1)));
        assertThat(g.atom(1), is(h.atom(0)));
        assertThat(g.atom(2), is(h.atom(3)));
        assertThat(g.atom(3), is(h.atom(2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPermutation() {
        ChemicalGraph g = new ChemicalGraph(2);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        g.addEdge(new Edge(2, 3, Bond.IMPLICIT));
        g.permute(new int[2]);
    }

    @Test public void sort() {
        ChemicalGraph g = new ChemicalGraph(2);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addEdge(new Edge(3, 2, Bond.IMPLICIT));
        g.addEdge(new Edge(1, 2, Bond.IMPLICIT));
        g.addEdge(new Edge(0, 3, Bond.IMPLICIT));
        g.addEdge(new Edge(0, 1, Bond.IMPLICIT));
        assertThat(g.edges(0), CoreMatchers
                .<List<Edge>>is(Arrays.asList(new Edge(0, 3, Bond.IMPLICIT),
                                              new Edge(0, 1, Bond.IMPLICIT))));
        assertThat(g.edges(1), CoreMatchers
                .<List<Edge>>is(Arrays.asList(new Edge(1, 2, Bond.IMPLICIT),
                                              new Edge(1, 0, Bond.IMPLICIT))));
        assertThat(g.edges(2), CoreMatchers
                .<List<Edge>>is(Arrays.asList(new Edge(2, 3, Bond.IMPLICIT),
                                              new Edge(2, 1, Bond.IMPLICIT))));
        assertThat(g.edges(3), CoreMatchers
                .<List<Edge>>is(Arrays.asList(new Edge(3, 2, Bond.IMPLICIT),
                                              new Edge(3, 0, Bond.IMPLICIT))));
        g.sort();
        assertThat(g.edges(0), CoreMatchers
                .<List<Edge>>is(Arrays.asList(new Edge(0, 1, Bond.IMPLICIT),
                                              new Edge(0, 3, Bond.IMPLICIT))));
        assertThat(g.edges(1), CoreMatchers
                .<List<Edge>>is(Arrays.asList(new Edge(1, 0, Bond.IMPLICIT),
                                              new Edge(1, 2, Bond.IMPLICIT))));
        assertThat(g.edges(2), CoreMatchers
                .<List<Edge>>is(Arrays.asList(new Edge(2, 1, Bond.IMPLICIT),
                                              new Edge(2, 3, Bond.IMPLICIT))));
        assertThat(g.edges(3), CoreMatchers
                .<List<Edge>>is(Arrays.asList(new Edge(3, 0, Bond.IMPLICIT),
                                              new Edge(3, 2, Bond.IMPLICIT))));
    }

    @Test public void atoms() {
        ChemicalGraph g = new ChemicalGraph(20);
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        g.addAtom(mock(Atom.class));
        Iterable<Atom> atoms = g.atoms();
        Iterator<Atom> it    = atoms.iterator();
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertFalse(it.hasNext());
    }

    @Test public void configurationOf() throws Exception {
        ChemicalGraph g = ChemicalGraph.fromSmiles("O[C@]12CCCC[C@@]1(O)CCCC2");

        Assert.assertThat(g.configurationOf(1), is(Configuration.TH1));
        Assert.assertThat(g.configurationOf(6), is(Configuration.TH1));
    }

    @Test public void configurationOf_myoInositol() throws Exception {
        ChemicalGraph g = ChemicalGraph.fromSmiles("O[C@@H]1[C@H](O)[C@H](O)[C@H](O)[C@H](O)[C@@H]1O");

        Assert.assertThat(g.configurationOf(1), is(Configuration.TH1));
        Assert.assertThat(g.configurationOf(2), is(Configuration.TH1));
        Assert.assertThat(g.configurationOf(4), is(Configuration.TH1));
        Assert.assertThat(g.configurationOf(6), is(Configuration.TH1));
        Assert.assertThat(g.configurationOf(8), is(Configuration.TH1));
        Assert.assertThat(g.configurationOf(10), is(Configuration.TH2));
    }

}
