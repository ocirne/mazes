
import React from 'react';

interface CellProps {
    x: number,
    y: number;
}

const Cell: React.FC<CellProps> = (props) => (
    <div>Cell ({props.x}, {props.y})</div>
)

export default Cell;
