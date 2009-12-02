/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 */
package org.xwiki.plexus.lifecycle.phase;

import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.lifecycle.phase.AbstractPhase;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.PhaseExecutionException;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;

public class InitializePhase extends AbstractPhase
{
    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.plexus.lifecycle.phase.AbstractPhase#execute(java.lang.Object,
     *      org.codehaus.plexus.component.manager.ComponentManager, org.codehaus.plexus.classworlds.realm.ClassRealm)
     */
    @Override
    public void execute(Object object, ComponentManager componentManager, ClassRealm classRealm)
        throws PhaseExecutionException
    {
        if (object instanceof Initializable) {
            try {
                ((Initializable) object).initialize();
            } catch (InitializationException e) {
                throw new PhaseExecutionException("Error initializing component", e);
            }
        }
    }
}
