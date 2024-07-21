
import React from 'react';
import Cell from './Cell';

const Maze: React.FC = () => (
    <div>
        <div>
            <Cell x={0} y={1} />
            <Cell x={1} y={1} />
        </div>
        <div>
            <Cell x={0} y={1} />
            <Cell x={1} y={1} />
        </div>
    </div>
)

export default Maze;
