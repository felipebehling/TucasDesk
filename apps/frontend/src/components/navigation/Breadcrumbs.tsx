import { Link } from "react-router-dom";
import "./breadcrumbs.css";

export interface BreadcrumbItem {
  label: string;
  to?: string;
}

interface BreadcrumbsProps {
  items: BreadcrumbItem[];
}

/**
 * A breadcrumb navigation component.
 *
 * @param {BreadcrumbsProps} props - The component props.
 * @returns {JSX.Element | null} The rendered breadcrumbs, or null if there are no items.
 */
export function Breadcrumbs({ items }: BreadcrumbsProps) {
  if (!items.length) {
    return null;
  }

  return (
    <nav className="breadcrumbs" aria-label="Breadcrumb">
      <ol>
        {items.map((item, index) => {
          const isLast = index === items.length - 1;
          return (
            <li key={item.label} className={isLast ? "is-active" : undefined}>
              {item.to && !isLast ? <Link to={item.to}>{item.label}</Link> : <span>{item.label}</span>}
            </li>
          );
        })}
      </ol>
    </nav>
  );
}

export default Breadcrumbs;
